import SwiftUI
import shared

struct DriversView: View {

    @StateObject private var vm = DriversObservableViewModel()

    @State private var errorMessage: String?
    @State private var showError = false

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
        .onAppear {
            vm.onShowError = { message in
                errorMessage = message
                showError = true
            }
        }
        .alert("Error", isPresented: $showError, presenting: errorMessage) { _ in
            Button("OK", role: .cancel) {}
        } message: { message in
            Text(message)
        }
    }
}

#Preview {
    NavigationStack {
        DriversView()
    }
}
