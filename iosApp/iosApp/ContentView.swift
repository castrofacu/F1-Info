import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            NavigationStack {
                DriversView()
            }
            .tabItem {
                Label("Pilotos", systemImage: "person.2.fill")
            }

            NavigationStack {
                RaceReplayView()
            }
            .tabItem {
                Label("Carrera", systemImage: "flag.checkered")
            }
        }
        .tint(.f1Red)
    }
}

#Preview {
    ContentView()
}
