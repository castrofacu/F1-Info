import Foundation
import SharedLogic

enum KoinHelper {

    static func start() {
        SharedModuleKt.doInitKoin()
    }

    static func get<T>() -> T {
        return KoinApplication.shared.koin.get() as! T
    }
}
