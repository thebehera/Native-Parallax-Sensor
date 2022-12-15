//
//  ViewController.swift
//  Sensor
//
//  Created by Rahul Behera on 12/5/22.
//

import UIKit
import CoreMotion


class ViewController: UIViewController {
    @IBOutlet weak public var imageView: UIImageView!
    let motion = CMMotionManager()
    override func viewWillAppear(_ animated: Bool) {
      super.viewWillAppear(animated)
      if motion.isDeviceMotionAvailable {
        motion.showsDeviceMovementDisplay = true
        motion.deviceMotionUpdateInterval = 1.0 / 60.0
        var initial = CMRotationMatrix()
        var eventCount = 0
        motion.startDeviceMotionUpdates(
          using: .xArbitraryZVertical,
          to: OperationQueue.main
        ) {
          (deviceMotion, error) in
          guard let current = deviceMotion?.attitude.rotationMatrix else {
            return
          }
          eventCount += 1
          if eventCount > 1 {
            let elevation = 100.0
            self.imageView.transform.tx =
              -(initial.m31 * current.m11
              + initial.m32 * current.m12
              + initial.m33 * current.m13) * elevation
            self.imageView.transform.ty =
              -(initial.m31 * current.m21
              + initial.m32 * current.m22
              + initial.m33 * current.m23) * elevation
          } else {
            initial = current
          }
        }
      }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        motion.stopDeviceMotionUpdates()
    }
}
