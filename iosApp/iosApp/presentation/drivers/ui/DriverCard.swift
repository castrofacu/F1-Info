import SwiftUI
import shared

struct DriverCard: View {
    let driver: Driver

    var body: some View {
        HStack(spacing: 12) {

            Rectangle()
                .fill(Color(hex: driver.teamColour))
                .frame(width: 4, height: 44)
                .clipShape(Capsule())

            AsyncImage(url: driver.headshotUrl.flatMap { URL(string: $0) }) { phase in
                switch phase {
                case .success(let image):
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 44, height: 44)
                        .clipShape(Circle())
                case .failure, .empty:
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .frame(width: 44, height: 44)
                        .foregroundStyle(.secondary)
                @unknown default:
                    EmptyView()
                }
            }

            VStack(alignment: .leading, spacing: 2) {
                Text(driver.fullName)
                    .font(.body)
                    .fontWeight(.medium)

                Text(driver.teamName)
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }

            Spacer()

            Text("\(driver.number)")
                .font(.title2)
                .fontWeight(.bold)
                .monospacedDigit()
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }
}
