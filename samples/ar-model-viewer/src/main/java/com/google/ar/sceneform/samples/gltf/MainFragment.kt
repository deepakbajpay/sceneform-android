package com.google.ar.sceneform.samples.gltf

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.SceneView
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.gorisse.thomas.sceneform.scene.await
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main), View.OnClickListener {

    private var childCount: Int = 0
    private lateinit var arFragment: ArFragment
    private val arSceneView get() = arFragment.arSceneView
    private val scene get() = arSceneView.scene

    private var model: Renderable? = null

    //    private var modelView: ViewRenderable? = null
    private lateinit var adapter: ItemAdapter

    lateinit var rv: RecyclerView
    lateinit var button: ImageView
    lateinit var cartGroup: Group
    lateinit var itemCountTv:TextView

    val itemList: ArrayList<Int> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList()

        arFragment = (childFragmentManager.findFragmentById(R.id.arFragment) as ArFragment).apply {
            setOnSessionConfigurationListener { session, config ->
                // Modify the AR session configuration here
            }
            setOnViewCreatedListener { arSceneView ->
                arSceneView.setFrameRateFactor(SceneView.FrameRate.FULL)
            }
            setOnTapArPlaneListener(::onTapPlane)
        }

        rv = view.findViewById(R.id.item_rv)
        adapter = ItemAdapter(itemList, ::onItemClick)
        rv.adapter = adapter

        cartGroup = view.findViewById(R.id.cart_group)
        itemCountTv = view.findViewById(R.id.count_tv)
        button = view.findViewById(R.id.button_iv)
        button.setOnClickListener {
            rv.isVisible = !rv.isVisible
            cartGroup.isVisible = !rv.isVisible
        }

        lifecycleScope.launchWhenCreated {
            loadModels("side_table_tall.glb")
        }
    }

    private fun createList() {
        itemList.add(R.drawable.table)
        itemList.add(R.drawable.side_table_tall)
        itemList.add(R.drawable.vase)
        itemList.add(R.drawable.chair)
        itemList.add(R.drawable.wooden_table)
        itemList.add(R.drawable.victorian_side_table)
        itemList.add(R.drawable.desk_lamp)
        itemList.add(R.drawable.modern__sofa)
        itemList.add(R.drawable.indoor_plant)
        itemList.add(R.drawable.office_chair)
        itemList.add(R.drawable.velvet_bean_bag)
        itemList.add(R.drawable.recliner)
        itemList.add(R.drawable.office_chair_2)
    }

    private fun onItemClick(drawable: Int) {
        rv.isVisible = false
        cartGroup.isVisible = true
        button.setImageResource(drawable)

        // scene.children?.forEach{node->scene.removeChild(node)}

        val model = when (drawable) {
            R.drawable.table -> "table.glb"
            R.drawable.side_table_tall -> "side_table_tall.glb"
            R.drawable.vase -> "vase.glb"
            R.drawable.wooden_table -> "wooden_table.glb"
            R.drawable.chair -> "chair.glb"
            R.drawable.modern__sofa -> "modern__sofa.glb"
            R.drawable.desk_lamp -> "desk_lamp.glb"
            R.drawable.indoor_plant -> "indoor_plant.glb"
            R.drawable.office_chair_2 -> "office_chair_2.glb"
            R.drawable.velvet_bean_bag -> "velvet_bean_bag.glb"
            R.drawable.recliner -> "recliner.glb"
            R.drawable.office_chair -> "office_chair.glb"
            R.drawable.victorian_side_table -> "victorian_side_table.glb"
            else -> "table.glb"
        }

        lifecycleScope.launch { loadModels(model) }

    }

    private suspend fun loadModels(modelName: String) {
        model = ModelRenderable.builder()
            .setSource(context, Uri.parse("models/$modelName"))
            .setIsFilamentGltf(true)
            .await()
        /* modelView = ViewRenderable.builder()
             .setView(context, R.layout.view_renderable_infos)
             .await()*/
    }

    private fun onTapPlane(hitResult: HitResult, plane: Plane, motionEvent: MotionEvent) {
        if (model == null /*|| modelView == null*/) {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            return
        }

        // Create the Anchor.
        scene.addChild(AnchorNode(hitResult.createAnchor()).apply {
            itemCountTv.text = "${++childCount}"
            // Create the transformable model and add it to the anchor.
            addChild(TransformableNode(arFragment.transformationSystem).apply {
                renderable = model
                renderableInstance.setCulling(false)
                renderableInstance.animate(true).start()
                // Add the View
                /* addChild(Node().apply {
                     // Define the relative position
                     localPosition = Vector3(0.0f, 1f, 0.0f)
                     localScale = Vector3(0.3f, 0.3f, 0.3f)
                     renderable = modelView
                 })*/
            })
        })
    }

    override fun onClick(v: View?) {
        rv.visibility = View.VISIBLE
    }
}