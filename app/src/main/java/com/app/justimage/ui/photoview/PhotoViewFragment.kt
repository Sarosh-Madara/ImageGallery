package com.app.justimage.ui.photoview

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.app.justimage.R
import com.app.justimage.data.PixabayPhoto
import com.app.justimage.databinding.FragmentPhotoViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class PhotoViewFragment : Fragment(R.layout.fragment_photo_view) {
    private lateinit var bitmap: Bitmap
    private val args by navArgs<PhotoViewFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentPhotoViewBinding.bind(view)

        binding.apply {
            val photo = args.photo
            Glide.with(this@PhotoViewFragment)
                    .load(photo.largeImageURL)
                    .error(R.drawable.ic_error)
                    .listener(object : RequestListener<Drawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            progressBar2.isVisible = false
                            binding.layoutControls.isVisible = false
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            progressBar2.isVisible = false

                            binding.layoutControls.isVisible = true
                            bitmap = (resource as BitmapDrawable ).getBitmap()
                            return false
                        }
                    })
                    .into(imageView)

            initListeners(binding, photo)


        }
    }

    private fun initListeners(binding: FragmentPhotoViewBinding, photo: PixabayPhoto) {
        binding.clapShare.setOnClickListener(View.OnClickListener {
            // share image
            shareImage(binding)
        })

        binding.clapDownload.setOnClickListener {
            // image download
            downloadImage(binding, photo);
        }
    }

    private fun shareImage(binding: FragmentPhotoViewBinding) {
        binding.imageView.buildDrawingCache()
        val icon: Bitmap = binding.imageView.drawingCache
        val share = Intent(Intent.ACTION_SEND)
        share.type = "image/*"
        val bytes = ByteArrayOutputStream()
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val f = File(Environment.getDataDirectory(), "tempfile.jpg")
        try {
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(f.absolutePath))
        startActivity(Intent.createChooser(share, "Share Image"))
    }

    private fun downloadImage(binding: FragmentPhotoViewBinding, photo: PixabayPhoto) {
        if (TedPermission.isGranted(context, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            binding.imageView.buildDrawingCache()
            val icon: Bitmap = binding.imageView.drawingCache
            val share = Intent(Intent.ACTION_SEND)
            share.type = "image/jpeg"
            val bytes = ByteArrayOutputStream()
            icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val f = File(Environment.getRootDirectory(), "/justimages/" + photo.id)
            try {
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else
            TedPermission.with(context)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {

        }

        override fun onPermissionDenied(deniedPermissions: List<String?>) {
            Toast.makeText(context, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT).show()
        }
    }
}