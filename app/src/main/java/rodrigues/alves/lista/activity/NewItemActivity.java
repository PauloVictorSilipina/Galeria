package rodrigues.alves.lista.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import rodrigues.alves.lista.R;
import rodrigues.alves.lista.model.NewItemActivityViewModel;

public class NewItemActivity extends AppCompatActivity {

    static int PHOTO_PICKER_REQUEST=1;
    @Override

    //Método onCreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        NewItemActivityViewModel vm = new ViewModelProvider(this).get(NewItemActivityViewModel.class);

        Uri selectPhotoLocation = vm.getSelectedPhotoLocation();
        if(selectPhotoLocation != null) {
            ImageView imvfotoPreview = findViewById(R.id.imvfotoPreview);
            imvfotoPreview.setImageURI(selectPhotoLocation);
        }

        //Obtenção do ImageButton
        ImageButton imgCI = findViewById(R.id.imbCI);
        //Ouvidor de cliques
        imgCI.setOnClickListener(new View.OnClickListener() {
            @Override
            //Método para abrir a galeria criando um intent implicito
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*"); //Seta o intent para documentos image/*
                startActivityForResult(photoPickerIntent, PHOTO_PICKER_REQUEST);
            }
        });

        //Botão com ouvidor de click
        Button btnAddItem = findViewById(R.id.btnAddItem);

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Aviso para dizer que precisa adicionar imagem
                if(selectPhotoLocation == null) {
                    Toast.makeText(NewItemActivity.this, "É necessário selecionar uma imagem!",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                EditText etTitle = findViewById(R.id.etTitle);
                String title = etTitle.getText().toString();

                //Aviso para adicionar titutlo
                if(title.isEmpty()) {
                    Toast.makeText(NewItemActivity.this, "É necessário inserir um título!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                EditText etDesc = findViewById(R.id.etDesc);
                String description = etDesc.getText().toString();

                //Aviso para adicionar descrição
                if (description.isEmpty()) {
                    Toast.makeText(NewItemActivity.this, "É necessário inserir uma descrição",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                //Criação da nova intent
                Intent i = new Intent();
                i.setData(selectPhotoLocation); //Set do Uri da img
                i.putExtra("title", title); //Seta titulo
                i.putExtra("description", description); //Seta desc
                setResult(Activity.RESULT_OK, i); //Resultado da activity
                finish();

            }
        });
    }

    @Override

    //Criação do método onActivityResult que recebe 3 parâmetros
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Verifica se foi feito um pedido para selecionar fotos
        if(requestCode == PHOTO_PICKER_REQUEST) {
            //Verifica se o resultCode deu sucesso
            if(resultCode == Activity.RESULT_OK) {
                Uri photoSelected = data.getData();
                ImageView imvfotoPreview = findViewById(R.id.imvfotoPreview);

                imvfotoPreview.setImageURI(photoSelected);

                NewItemActivityViewModel vm = new ViewModelProvider(this).get(NewItemActivityViewModel.class);
                vm.setSelectedPhotoLocation(photoSelected);
            }
        }
    }
}