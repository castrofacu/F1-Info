// DriversObservableViewModel.swift
//
// PATRÓN: ViewModel Wrapper (ObservableObject)
//
// Problema: El BaseViewModel de Kotlin extiende androidx.lifecycle.ViewModel,
// que no es directamente observable por SwiftUI. Tampoco podemos usar
// koin-androidx-compose (es solo Android).
//
// Solución: Creamos un ObservableObject de Swift que:
//   1. Obtiene el DriversViewModel de Kotlin vía ViewModelHelper (Koin)
//   2. Observa el StateFlow<DriversState> con un Task de Swift (gracias a SKIE)
//   3. Expone el estado como @Published para que SwiftUI lo redibuje automáticamente
//
// SKIE convierte StateFlow<DriversState> en SkieSwiftStateFlow<DriversState>,
// que implementa AsyncSequence. Esto permite usar "for await" de Swift concurrency.
//
// Equivalente Android: no existe — en Android koinViewModel() hace todo esto
// automáticamente dentro del ciclo de vida del Composable.

import Foundation
import shared  // Framework KMP

/// Wrapper SwiftUI-compatible del DriversViewModel de Kotlin.
///
/// @MainActor garantiza que todas las actualizaciones de @Published
/// ocurran en el hilo principal, igual que LiveData/StateFlow en Android
/// que se observan en el main thread con collectAsState().
@MainActor
final class DriversObservableViewModel: ObservableObject {

    // El ViewModel de Kotlin con toda la lógica de negocio.
    // Creado por Koin via ViewModelHelper para que las dependencias sean inyectadas.
    private let viewModel: DriversViewModel

    // @Published es el equivalente a StateFlow.collectAsState() en Compose:
    // cada vez que cambia, SwiftUI redibuja las vistas que dependen de él.
    @Published private(set) var state: DriversState

    // El error de alerta se publica como estado en vez de manejarse con un closure.
    // Esto evita la race condition donde un efecto ShowError llega antes de que
    // la View asigne el handler onShowError. Con @Published, SwiftUI reacciona
    // en cuanto el valor cambia, sin importar cuándo se suscribió la View.
    // Equivalente Android: los efectos también pueden perderse si el collector
    // no está activo; la solución es la misma — convertirlos a estado.
    @Published private(set) var alertMessage: String? = nil

    // Guardamos las Tasks para poder cancelarlas si la vista desaparece,
    // evitando memory leaks (similar a viewModelScope en Kotlin).
    private var stateObservationTask: Task<Void, Never>?
    private var effectObservationTask: Task<Void, Never>?

    init() {
        // Koin crea el DriversViewModel e inyecta GetDriversUseCase automáticamente.
        // ViewModelHelper es un KoinComponent — patrón estándar de Koin para iOS KMP.
        // En Android, koinViewModel() en el Composable hace esto automáticamente.
        self.viewModel = ViewModelHelper().driversViewModel()

        // state.value es optional porque BaseViewModel es genérico y SKIE no puede
        // garantizar non-null en type parameters. El `!` es seguro aquí: DriversState
        // siempre tiene un valor inicial (DriversState()) definido en el constructor.
        self.state = viewModel.state.value!

        startObservingState()
        startObservingEffects()
    }

    deinit {
        // Cancelamos las Tasks al destruir el wrapper para evitar fugas de memoria.
        // En Android el framework cancela viewModelScope automáticamente via onCleared().
        // En iOS no hay ViewModelStore, así que cancelamos las Tasks de observación aquí.
        // No llamamos onCleared()/clear() del ViewModel de Kotlin porque ese hook
        // pertenece al ciclo de vida de Android y llamarlo manualmente causaría
        // doble invocación si Android también lo llama.
        stateObservationTask?.cancel()
        effectObservationTask?.cancel()
    }

    /// Inicia la observación del StateFlow de estado.
    /// SKIE hace que viewModel.state sea un AsyncSequence,
    /// por eso podemos usar "for await" directamente.
    private func startObservingState() {
        stateObservationTask = Task {
            // for await es el equivalente Swift a .collect { } en coroutines de Kotlin.
            for await newState in viewModel.state {
                // newState es optional por el type erasure del genérico BaseViewModel.
                // guard descarta nils sin interrumpir el loop (continue en vez de break).
                guard let newState else { continue }
                self.state = newState
            }
        }
    }

    /// Inicia la observación del Channel de efectos (eventos one-shot).
    /// Equivalente al LaunchedEffect(Unit) { viewModel.effect.collect {} } de Android.
    private func startObservingEffects() {
        effectObservationTask = Task {
            for await effect in viewModel.effect {
                guard let effect else { continue }
                // onEnum(of:) es generado por SKIE para los sealed interfaces de Kotlin.
                switch onEnum(of: effect) {
                case .showError(let e):
                    alertMessage = e.message
                case .navigateToDriverDetail:
                    // TODO: implementar navegación a detalle del piloto
                    break
                }
            }
        }
    }

    /// Delega los intents al ViewModel de Kotlin.
    /// Equivalente a viewModel.handleIntent() en los Composables de Android.
    func handleIntent(_ intent: DriversIntent) {
        viewModel.handleIntent(intent: intent)
    }

    /// Limpia la alerta luego de que el usuario la descarta.
    func dismissAlert() {
        alertMessage = nil
    }
}
