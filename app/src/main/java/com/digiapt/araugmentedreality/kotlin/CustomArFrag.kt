package com.digiapt.araugmentedreality.kotlin

import android.util.Log
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class CustomArFrag : ArFragment() {

    override fun getSessionConfiguration(session: Session?): Config {

        val config = Config(session)
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)
        config.setFocusMode(Config.FocusMode.AUTO)
        session?.configure(config)

        this.arSceneView.setupSession(session)

        Log.d("test_1234", "check: ")

        (activity as MainActivity).setUpDataBase(config, session!!)

        return config
    }


}