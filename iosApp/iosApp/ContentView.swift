import UIKit
import SwiftUI
import shared

struct ContentView: UIViewControllerRepresentable {
	func makeUIViewController(context: Self.Context) -> UIViewController {
		MainViewControllerKt.MainViewController()
	}

	func updateUIViewController(_ uiViewController: UIViewController, context: Self.Context) {}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
			.ignoresSafeArea()
	}
}