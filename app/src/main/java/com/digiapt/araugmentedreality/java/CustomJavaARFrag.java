package com.digiapt.araugmentedreality.java;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.sceneform.ux.ArFragment;

public class CustomJavaARFrag extends ArFragment {

    @Override
    protected Config getSessionConfiguration(Session session) {

        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        config.setFocusMode(Config.FocusMode.AUTO);

        this.getArSceneView().setupSession(session);

        ((JavaAcitivity)getActivity()).setUpDatabase(config, session);

        return config;
    }
}
