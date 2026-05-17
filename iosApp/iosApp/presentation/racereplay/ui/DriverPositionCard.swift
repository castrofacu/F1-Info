import SwiftUI
import shared

struct DriverPositionCard: View {
    let driver: DriverPosition

    var body: some View {
        HStack(spacing: 16) {

            Text(driver.position.map { "\($0)" } ?? "N/A")
                .font(.title3)
                .fontWeight(.bold)
                .frame(width: 32, alignment: .center)
                .monospacedDigit()

            AsyncImage(url: driver.headshotUrl.flatMap { URL(string: $0) }) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 52, height: 52)
                        .clipShape(Circle())
                case .failure, .empty:
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .frame(width: 52, height: 52)
                        .foregroundStyle(.secondary)
                @unknown default:
                    EmptyView()
                }
            }

            Text(driver.name)
                .font(.body)
                .fontWeight(.medium)

            Spacer()

            Text(driver.teamName)
                .font(.caption)
                .fontWeight(.semibold)
                .foregroundStyle(Color(hex: driver.teamColour))
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 10)
    }
}
