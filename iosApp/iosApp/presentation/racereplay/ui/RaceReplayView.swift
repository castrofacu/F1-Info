import SwiftUI
import shared

struct RaceReplayView: View {

    @StateObject private var vm = RaceReplayObservableViewModel()

    var body: some View {
        ZStack(alignment: .bottomTrailing) {

            Group {
                if vm.state.isLoading {
                    LoadingView()
                } else if let error = vm.state.error {
                    ErrorView(message: error) {
                        vm.handleIntent(RaceReplayIntentRetryLoad())
                    }
                } else {
                    List(vm.state.drivers, id: \.number) { driver in
                        DriverPositionCard(driver: driver)
                            .listRowInsets(EdgeInsets())
                            .listRowSeparator(.hidden)
                    }
                    .listStyle(.plain)
                    .animation(.easeInOut(duration: 0.3), value: vm.state.drivers.map { $0.number })
                }
            }

            Button {
                vm.handleIntent(RaceReplayIntentPlayStop())
            } label: {
                Image(systemName: vm.state.isPlaying ? "stop.fill" : "play.fill")
                    .font(.title2)
                    .foregroundStyle(.white)
                    .frame(width: 56, height: 56)
                    .background(Color.f1Red)
                    .clipShape(Circle())
                    .shadow(color: .black.opacity(0.3), radius: 8, x: 0, y: 4)
            }
            .padding(.trailing, 20)
            .padding(.bottom, 20)
        }
        .navigationTitle("Abu Dhabi GP 2025")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            if vm.state.isPlaying && !vm.state.currentRaceTime.isEmpty {
                ToolbarItem(placement: .topBarTrailing) {
                    Label(vm.state.currentRaceTime, systemImage: "clock")
                        .font(.caption)
                        .foregroundStyle(.secondary)
                }
            }
        }
        .onAppear {
            vm.handleIntent(RaceReplayIntentLoadRaceData())
        }
        .onDisappear {
            vm.stopPlaybackIfNeeded()
        }
        .alert("Error", isPresented: Binding(
            get: { vm.alertMessage != nil },
            set: { if !$0 { vm.dismissAlert() } }
        )) {
            Button("OK", role: .cancel) {}
        } message: {
            Text(vm.alertMessage ?? "")
        }
    }
}

#Preview {
    NavigationStack {
        RaceReplayView()
    }
}
