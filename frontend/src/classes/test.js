/**
 * @author mrdoob / http://mrdoob.com/
 * @author Mugen87 / https://github.com/Mugen87
 * @edits obecerra3 / https://github.com/obecerra3
 Pointer lock controls using requirejs with z axis as up vector
 */

define(["three"], (THREE) => {
    var PointerLockControls = function ( camera, domElement ) {

    	if ( domElement === undefined ) {
    		console.warn( 'THREE.PointerLockControls: The second parameter "domElement" is now mandatory.' );
    		domElement = document.body;
    	}

    	this.domElement = domElement;
    	this.isLocked = false;

    	//
    	// internals
    	//

    	var scope = this;

    	var changeEvent = { type: 'change' };
    	var lockEvent = { type: 'lock' };
    	var unlockEvent = { type: 'unlock' };

        // change to account for z up
    	var euler = new THREE.Euler( 0, 0, 0, 'ZYX' );

    	var PI_2 = Math.PI / 2;

        // new constraints
        var MAX_PI = Math.PI / 1.1;
        var MIN_PI = 0;

    	var vec = new THREE.Vector3();

    	function onMouseMove( event ) {

    		if ( scope.isLocked === false ) return;

    		var movementX = event.movementX || event.mozMovementX || event.webkitMovementX || 0;
    		var movementY = event.movementY || event.mozMovementY || event.webkitMovementY || 0;

    		euler.setFromQuaternion( camera.quaternion );

            // change to account for z up
            euler.z -= movementX * 0.002;
    		euler.x -= movementY * 0.002;

            // new constraints
            euler.x = Math.max(MIN_PI, Math.min(MAX_PI, euler.x));

    		camera.quaternion.setFromEuler( euler );

    		scope.dispatchEvent( changeEvent );

    	}

    	function onPointerlockChange() {

    		if ( document.pointerLockElement === scope.domElement ) {

    			scope.dispatchEvent( lockEvent );

    			scope.isLocked = true;

    		} else {

    			scope.dispatchEvent( unlockEvent );

    			scope.isLocked = false;

    		}

    	}

    	function onPointerlockError() {

    		console.error( 'THREE.PointerLockControls: Unable to use Pointer Lock API' );

    	}

    	this.connect = function () {

    		document.addEventListener( 'mousemove', onMouseMove, false );
    		document.addEventListener( 'pointerlockchange', onPointerlockChange, false );
    		document.addEventListener( 'pointerlockerror', onPointerlockError, false );

    	};

    	this.disconnect = function () {

    		document.removeEventListener( 'mousemove', onMouseMove, false );
    		document.removeEventListener( 'pointerlockchange', onPointerlockChange, false );
    		document.removeEventListener( 'pointerlockerror', onPointerlockError, false );

    	};

    	this.dispose = function () {

    		this.disconnect();

    	};

    	this.getObject = function () { // retaining this method for backward compatibility

    		return camera;

    	};

    	this.getDirection = function () {

    		var direction = new THREE.Vector3( 0, 0, -1 );

    		return function ( v ) {

    			return v.copy( direction ).applyQuaternion( camera.quaternion );

    		};

    	}();

    	this.moveForward = function ( distance ) {

    		// move forward parallel to the xz-plane
    		// assumes camera.up is y-up

    		vec.setFromMatrixColumn( camera.matrix, 0 );

    		vec.crossVectors( camera.up, vec );

    		camera.position.addScaledVector( vec, distance );

    	};

    	this.moveRight = function ( distance ) {

    		vec.setFromMatrixColumn( camera.matrix, 0 );

    		camera.position.addScaledVector( vec, distance );

    	};

    	this.lock = function () {

    		this.domElement.requestPointerLock();

    	};

    	this.unlock = function () {

    		document.exitPointerLock();

    	};

    	this.connect();

    };

    PointerLockControls.prototype = Object.create( THREE.EventDispatcher.prototype );
    PointerLockControls.prototype.constructor = PointerLockControls;

    return PointerLockControls;
});