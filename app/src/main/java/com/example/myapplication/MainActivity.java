package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.db.ProductDB;
import com.example.myapplication.model.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    ProductDB dbHelper;
    ArrayList<Product> listview_Products;
    Product produto;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnCadastrar = (Button) findViewById(R.id.btn_Cadastrar);
        btnCadastrar.setOnClickListener(new android.view.View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, ProductsForm.class);
                startActivity(intent);
            }
        });

        lista = (ListView) findViewById(R.id.listview_Products);
        registerForContextMenu(lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Product produtoEscolhido = (Product) adapter.getItemAtPosition(position);

                Intent i = new Intent(MainActivity.this, ProductsForm.class);
                i.putExtra("produto-escolhido", produtoEscolhido);
                startActivity(i);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id){
                produto = (Product) adapter.getItemAtPosition(position);
                return false;
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuDelete = menu.add("Deletar Produto");
        menuDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){

                dbHelper = new ProductDB(MainActivity.this);
                dbHelper.deleteProduct(produto);
                dbHelper.close();

                loadProduct();

                return true;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadProduct();
    }

    public void loadProduct() {
        dbHelper = new ProductDB(MainActivity.this);
        listview_Products = dbHelper.getLista();
        dbHelper.close();

        if(listview_Products != null){
            adapter = new ArrayAdapter<Product>(MainActivity.this, android.R.layout.simple_list_item_1, listview_Products);
            lista.setAdapter(adapter);
        }

    }
}