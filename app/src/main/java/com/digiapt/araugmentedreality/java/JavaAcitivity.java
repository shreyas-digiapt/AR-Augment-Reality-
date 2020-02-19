package com.digiapt.araugmentedreality.java;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.digiapt.araugmentedreality.R;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.io.IOException;
import java.util.Collection;

public class JavaAcitivity extends AppCompatActivity implements Scene.OnUpdateListener {

    private CustomJavaARFrag arFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java_acitivity);

        arFrag = (CustomJavaARFrag) getSupportFragmentManager().findFragmentById(R.id.custom_java_ar_frag);
        arFrag.getArSceneView().getScene().addOnUpdateListener(this);

    }

    public void setUpDatabase(Config config, Session session) {
        try {
            Bitmap bitmap1 = BitmapFactory.decodeStream(getAssets().open("man_image.jpg"));
            Bitmap bitmap2 = BitmapFactory.decodeStream(getAssets().open("bottle_image.jpg"));
            AugmentedImageDatabase db = new AugmentedImageDatabase(session);
            db.addImage("man_java", bitmap1);
            db.addImage("bottle_java", bitmap2);
            config.setAugmentedImageDatabase(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        Frame frame = arFrag.getArSceneView().getArFrame();
        Collection<AugmentedImage> images = frame.getUpdatedTrackables(AugmentedImage.class);

        Log.d("test_123", "size: "+images.size());

        for (AugmentedImage image : images) {
            if (image.getTrackingState() == TrackingState.TRACKING) {
                if (image.getName().equals("man_java")) {
                   createAnchor(image, "Alien Animal.sfb");
                }else if(image.getName().equals("bottle_java")){
                    createAnchor(image, "ArcticFox_Posed.sfb");
                }
            }
        }
    }

    private void createAnchor(AugmentedImage image, String assetPath) {
        Anchor anchor = image.createAnchor(image.getCenterPose());
        createModel(anchor, assetPath);
    }

    private void createModel(Anchor anchor, String assetPath) {
        ModelRenderable.builder().setSource(this, Uri.parse(assetPath))
                .build()
                .thenAccept(modelRenderable -> {
                    placeModel(modelRenderable, anchor);
                });
    }

    private void placeModel(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode node = new AnchorNode(anchor);
        node.setRenderable(modelRenderable);
        arFrag.getArSceneView().getScene().addChild(node);
    }
}
