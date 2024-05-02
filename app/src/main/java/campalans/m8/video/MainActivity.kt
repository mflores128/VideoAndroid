package campalans.m8.video

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import campalans.m8.video.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var videoFile: File
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.videoView.setOnPreparedListener{mediaplayer->
            val mediaController = MediaController(this)
            mediaController.setAnchorView(binding.videoView)
            binding.videoView.setMediaController(mediaController)
        }
        binding.buttonvideo.setOnClickListener{
            createVideoFile()
            val intent=Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { videoIntent->
                videoIntent.resolveActivity(packageManager)?.also { cameraApp ->
                    val videoUri: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.fileprovider.fileprovider",
                        videoFile
                    )
                    videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
                }
            }
            startForResult.launch(intent)
        }


    }

    private fun createVideoFile() {
        val dir = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        videoFile= File.createTempFile("marc_${System.currentTimeMillis()}_",".mp4",dir)
    }
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val videoUri = Uri.fromFile(videoFile)
            binding.videoView.setVideoURI(videoUri)
            binding.videoView.start()
        }
    }
}