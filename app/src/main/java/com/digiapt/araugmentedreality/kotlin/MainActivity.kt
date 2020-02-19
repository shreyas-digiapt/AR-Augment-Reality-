package com.digiapt.araugmentedreality.kotlin

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.digiapt.araugmentedreality.R
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.rendering.ModelRenderable

class MainActivity : AppCompatActivity(), Scene.OnUpdateListener {

    private var customArFrag: CustomArFrag? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customArFrag = supportFragmentManager.findFragmentById(R.id.custom_ar_frag) as CustomArFrag
        customArFrag!!.arSceneView.scene.addOnUpdateListener(this)

    }

    fun setUpDataBase(config: Config, session: Session) {
        val bitmap = BitmapFactory.decodeStream(assets.open("man_image.jpg"))
        val db = AugmentedImageDatabase(session)
        db.addImage("man_image", bitmap)
        config.augmentedImageDatabase = (db)

        Log.d("test_1234", "sad: ${config.augmentedImageDatabase.numImages}")
    }

    override fun onUpdate(p0: FrameTime?) {

        val frame = customArFrag!!.arSceneView.arFrame
        val images = frame!!.getUpdatedTrackables(AugmentedImage::class.java) as Collection<AugmentedImage>

        Log.d("test_123", "size: ${images.size}")

        for (image in images) {
            if (image.trackingState == TrackingState.TRACKING) {
                Log.d("test_123", "name: ${image.name}")
                if (image.name.equals("man_image")) {
                    val anchor = image.createAnchor(image.centerPose)

                    createModel(anchor!!)
                }
            }
        }
    }

    private fun createModel(anchor: Anchor) {
        ModelRenderable.builder().setSource(this, Uri.parse("ArcticFox_Posed.sfb"))
            .build()
            .thenAccept{modelRenderable -> placeModel(modelRenderable, anchor) }
    }

    private fun placeModel(modelRenderable: ModelRenderable?, anchor: Anchor) {

        val anchorNode = AnchorNode(anchor)
        anchorNode.renderable = modelRenderable
        customArFrag!!.arSceneView.scene.addChild(anchorNode)
    }
}
