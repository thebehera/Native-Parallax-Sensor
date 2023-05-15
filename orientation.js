var sensitivityX = 180.0
var sensitivityY = 180.0

function requestOrientationPermission(){
    DeviceOrientationEvent.requestPermission()
    .then(response => {
        if (response == 'granted') {
            var buttonElement = document.getElementById('button');
            buttonElement.parentNode.removeChild(buttonElement);
            
            var image = document.getElementById('image');
            image.style.visibility = 'visible';
            window.addEventListener('deviceorientation', (e) => {
                var rotation = getRotationMatrix(event.alpha, event.beta, event.gamma)
                processMotion(rotation, (xAxisChange, yAxisChange) => {
                    var x = xAxisChange * sensitivityX
                    var y = yAxisChange * sensitivityY
                    image.style.transform = "translate(" + x + "px, " + y + "px )";
                })
            })
        }
    }).catch(console.error)
}

var initial = [
    0.0,    0.0,    0.0,
    0.0,    0.0,    0.0,
    0.0,    0.0,    0.0
];
var isInitialEventSet = false

function processMotion(current, callback) {
    if (isInitialEventSet) {
      var yAxisChange = // rd6
        -(initial[6] * current[0]
        + initial[7] * current[1]
        + initial[8] * current[2])
      var xAxisChange = // rd7
        initial[6] * current[3]
        + initial[7] * current[4]
        + initial[8] * current[5]
        callback(yAxisChange, xAxisChange)
    } else {
      initial = current
      isInitialEventSet = true
    }
}


// from the w3 spec
var degtorad = Math.PI / 180; // Degree-to-Radian conversion

function getRotationMatrix( alpha, beta, gamma ) {

  var _x = beta  ? beta  * degtorad : 0; // beta value
  var _y = gamma ? gamma * degtorad : 0; // gamma value
  var _z = alpha ? alpha * degtorad : 0; // alpha value

  var cX = Math.cos( _x );
  var cY = Math.cos( _y );
  var cZ = Math.cos( _z );
  var sX = Math.sin( _x );
  var sY = Math.sin( _y );
  var sZ = Math.sin( _z );

  //
  // ZXY rotation matrix construction.
  //

  var m11 = cZ * cY - sZ * sX * sY;
  var m12 = - cX * sZ;
  var m13 = cY * sZ * sX + cZ * sY;

  var m21 = cY * sZ + cZ * sX * sY;
  var m22 = cZ * cX;
  var m23 = sZ * sY - cZ * cY * sX;

  var m31 = - cX * sY;
  var m32 = sX;
  var m33 = cX * cY;

  return [
    m11,    m12,    m13,
    m21,    m22,    m23,
    m31,    m32,    m33
  ];

};
