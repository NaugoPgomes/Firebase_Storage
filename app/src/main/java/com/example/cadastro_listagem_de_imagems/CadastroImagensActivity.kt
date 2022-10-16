package com.example.cadastro_listagem_de_imagems

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_cadastro_imagens.*
import java.io.File

class CadastroImagensActivity : AppCompatActivity(), View.OnClickListener
{

    private var uri_imagem : Uri? = null
    private var storage: FirebaseStorage? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_cadastro_images, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_camera -> {
                getCameraImages()
                true
            }

            R.id.nav_galeria -> {
                getImageGallery()
                true
            }

            else -> false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_imagens)

        buttonUploadImagem.setOnClickListener(this)

        storage = Firebase.storage
    }

    override fun onClick(view: View?)
    {
        when(view?.id)
        {
            buttonUploadImagem.id ->
            {
                if(uri_imagem != null)
                {
                    registerImage()
                }
                else
                {
                    Toast.makeText(this, "Nenhuma imagem foi selecionada. ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getCameraImages()
    {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val autorizacao = "com.example.cadastro_listagem_de_imagems"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg")


            val resolver = contentResolver

            uri_imagem = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        else
        {
            val diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            val ImageName = diretorio.path + "/image" + System.currentTimeMillis() + ".jpg"

            val file = File(ImageName)

            uri_imagem = FileProvider.getUriForFile(baseContext,autorizacao,file)
        }


        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri_imagem)
        startActivityForResult(intent,22)
    }

    private fun getImageGallery()
    {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

        startActivityForResult(Intent.createChooser(intent, "Escolha uma Imagem"),11)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == 11 && data != null)
            {
                uri_imagem = data.data

                imageViewCadastro.setImageURI(uri_imagem)
            }
            else if(requestCode == 22 && uri_imagem != null)
            {
                imageViewCadastro.setImageURI(uri_imagem)
            }
        }
    }


    private fun registerImage()
    {

        val reference = storage!!.reference.child("imagens").child("uploasImagem" + System.currentTimeMillis() + ".jpg") // System.currentTimeMillis() Vai gerar números aleatórios que vai permitir uma imagem não ser salva com o mesmo nome da outra.

        var uploadTask = reference.putFile(uri_imagem!!)

        uploadTask.addOnSuccessListener{

            Toast.makeText(this, "Imagem cadastrada.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }.addOnFailureListener{
            Toast.makeText(this, "Erro no cadastro da imagem.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }


}