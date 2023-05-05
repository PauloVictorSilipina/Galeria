package rodrigues.alves.lista.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import rodrigues.alves.lista.R;
import rodrigues.alves.lista.adapter.MyAdapter;
import rodrigues.alves.lista.model.MainActivityViewModel;
import rodrigues.alves.lista.model.MyItem;

public class MainActivity extends AppCompatActivity {

    static int NEW_ITEM_REQUEST=1;
    MyAdapter myAdapter;
    @Override

    //Método onCreate que vai registrar um ouvidor de click ao gerar a intent
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fabAddItem = findViewById(R.id.fabAddNewItem);

        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            //Criação da intent
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NewItemActivity.class);
                startActivityForResult(i, NEW_ITEM_REQUEST);
            }
        });


        RecyclerView rvItens = findViewById(R.id.rvItens);
        MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
        List<MyItem> itens = vm.getItens();
        myAdapter = new MyAdapter(this, itens);//myadapter criado
        rvItens.setAdapter(myAdapter);//setamos o adapter no recycleview
        rvItens.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);//criado um gerenciador de layout linear
        rvItens.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvItens.getContext(),
                DividerItemDecoration.VERTICAL);
        rvItens.addItemDecoration(dividerItemDecoration);

    }

    @Override
    //Criação do método que irá gerar resultado
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Verifica se o requestCode é igual o request do item
        if(requestCode == NEW_ITEM_REQUEST) {
            //Caso o resultCode for certo, adiciona o novo item
            if(resultCode == Activity.RESULT_OK) {
                //Cria my item
                MyItem myItem = new MyItem();
                //Adicona titulo, descrição, fotoe adiciona myItem nos itens na tela
                myItem.title = data.getStringExtra("title");
                myItem.description = data.getStringExtra("description");
                Uri selectedPhotoURI = data.getData();

                MainActivityViewModel vm = new ViewModelProvider(this).get(MainActivityViewModel.class);
                List<MyItem> itens = vm.getItens();

                itens.add(myItem);
                myAdapter.notifyItemInserted(itens.size()-1);



                try {
                    //Função que carrega a imagem e guarda no bitmap
                    Bitmap photo = com.example.produtos.util.Util.getBitmap(MainActivity.this, selectedPhotoURI, 100, 100);
                    myItem.photo = photo; //Guarda bitmap em um objeto myitem
                } //Dispara exceção caso dê erro
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                itens.add(myItem);
                //Notifica que inseriu um item
                myAdapter.notifyItemInserted(itens.size()-1);
            }
        }
    }
}