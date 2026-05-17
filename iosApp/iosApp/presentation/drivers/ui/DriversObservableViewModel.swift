import Foundation
import shared

@MainActor
final class DriversObservableViewModel: ObservableObject {

    private let viewModel: DriversViewModel

    @Published private(set) var state: DriversState

    private var stateObservationTask: Task<Void, Never>?
    private var effectObservationTask: Task<Void, Never>?

    var onShowError: ((String) -> Void)?

    init() {
        self.viewModel = ViewModelHelper().driversViewModel()

        self.state = viewModel.state.value!

        startObservingState()
        startObservingEffects()
    }

    deinit {
        stateObservationTask?.cancel()
        effectObservationTask?.cancel()
        viewModel.clear()
    }

    private func startObservingState() {
        stateObservationTask = Task {
            for await newState in viewModel.state {
                guard let newState else { continue }
                self.state = newState
            }
        }
    }

    private func startObservingEffects() {
        effectObservationTask = Task {
            for await effect in viewModel.effect {
                guard let effect else { continue }
                switch onEnum(of: effect) {
                case .showError(let e):
                    onShowError?(e.message)
                case .navigateToDriverDetail:
                    break
                }
            }
        }
    }

    func handleIntent(_ intent: DriversIntent) {
        viewModel.handleIntent(intent: intent)
    }
}
