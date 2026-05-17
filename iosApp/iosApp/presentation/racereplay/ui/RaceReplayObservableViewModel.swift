import Foundation
import shared

@MainActor
final class RaceReplayObservableViewModel: ObservableObject {

    private let viewModel: RaceReplayViewModel

    @Published private(set) var state: RaceReplayState

    private var stateObservationTask: Task<Void, Never>?
    private var effectObservationTask: Task<Void, Never>?

    var onShowError: ((String) -> Void)?

    init() {
        self.viewModel = ViewModelHelper().raceReplayViewModel()
        self.state = viewModel.state.value!

        startObservingState()
        startObservingEffects()
    }

    deinit {
        stateObservationTask?.cancel()
        effectObservationTask?.cancel()
        viewModel.onCleared()
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
                }
            }
        }
    }

    func handleIntent(_ intent: RaceReplayIntent) {
        viewModel.handleIntent(intent: intent)
    }

    func stopPlaybackIfNeeded() {
        if state.isPlaying {
            viewModel.handleIntent(intent: RaceReplayIntentPlayStop())
        }
    }
}
