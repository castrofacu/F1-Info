import SwiftUI
import shared

struct DriversView: View {

    @StateObject private var vm = DriversObservableViewModel()

    var body: some View {
        Group {
            if vm.state.isLoading {
                LoadingView()
            } else if let error = vm.state.error {
                ErrorView(message: error) {
                    vm.handleIntent(DriversIntentRetryLoad())
                }
            } else {
                List(vm.state.drivers, id: \.number) { driver in
                    DriverCard(driver: driver)
                        .listRowInsets(EdgeInsets())
                        .listRowSeparator(.hidden)
                }
                .listStyle(.plain)
            }
        }
        .navigationTitle("Pilotos")
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
        DriversView()
    }
}
