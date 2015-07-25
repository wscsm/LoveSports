/*
 * Copyright (C) 2013 Steelkiwi Development, Julia Zudikova, Viacheslav Tiagotenkov
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skd.androidrecording.video;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.List;

/*
 * Manages camera preview
 */
@SuppressWarnings("deprecation")
public class CameraManager {

	private Camera camera;
	private int camerasCount;
	private int defaultCameraID;
	private int cameraRotationDegree;
	private boolean isPreviewStarted = false;
	
	public CameraManager() {
		camerasCount = CameraHelper.getAvailableCamerasCount();
		defaultCameraID = CameraHelper.getDefaultCameraID();
	}
	
	public void openCamera() {
		if (camera != null) {
			releaseCamera();
		}
		camera = Camera.open(defaultCameraID);
	}
	
	public void releaseCamera() {
		if (camera != null) {
			camera.release();
			camera = null;
		}
	}
	
	public void switchCamera() {
		stopCameraPreview();
		
		defaultCameraID = (defaultCameraID + 1) % camerasCount;
		openCamera();
	}

    // begin : added by suyanlu;
    public void setFlash(String mode){
        Parameters p = camera.getParameters();
        p.setFlashMode(mode);
        camera.setParameters(p);
    }

    public String getFlashMode(){
        Parameters p = camera.getParameters();
        return p.getFlashMode();
    }

    public List<String> getSupportedFlashModes() {
        Parameters p = camera.getParameters();
        return p.getSupportedFlashModes();
    }

	// 调整焦距,支持0和max两级;
	public void setZoom(){
        if (camera == null){
            return;
        }
		Parameters p = camera.getParameters();
		if (p.isZoomSupported()){
			int currentZoom = p.getZoom();
			int maxZoom = p.getMaxZoom();
			if (p.isSmoothZoomSupported()) {
				camera.startSmoothZoom(currentZoom == 0 ? maxZoom:0);
			}
			else{
				p.setZoom(currentZoom == 0 ? maxZoom:0);
                camera.setParameters(p);
			}
		}
	}
    // end : added by suyanlu;

    public void setupCameraAndStartPreview(SurfaceHolder sf, Size sz, int displayRotation) {
		stopCameraPreview();
		
		cameraRotationDegree = CameraHelper.setCameraDisplayOrientation(defaultCameraID, camera, displayRotation);
		
		Parameters param = camera.getParameters();
		param.setPreviewSize(sz.width, sz.height);
		camera.setParameters(param);
		
		if (setDisplay(sf)) {
			startCameraPreview();
		}	
	}
	
	public boolean setDisplay(SurfaceHolder sf) {
		try {
			camera.setPreviewDisplay(sf);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void startCameraPreview() {
		camera.startPreview();
		isPreviewStarted = true;
	}
	
	public void stopCameraPreview() {
		if (isPreviewStarted && (camera != null)) {
			isPreviewStarted = false;
			camera.stopPreview();
		}
	}

	public Camera getCamera() {
		return camera;
	}
	
	public int getCameraDisplayOrientation() {
		return (CameraHelper.isCameraFacingBack(defaultCameraID)) ? cameraRotationDegree : cameraRotationDegree + 180;
	}
	
	public boolean hasMultipleCameras() {
		return (camerasCount > 1);
	}

	public boolean isPreviewStarted() {
		return isPreviewStarted;
	}
}
