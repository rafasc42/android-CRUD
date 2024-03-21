package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.db.ProductDB;
import com.example.myapplication.model.Product;

public class ProductsForm extends AppCompatActivity {

    EditText edit_ProductName, edit_ProductDescription, edit_ProductQuantity;

    Button btn_Polimorf;
    Product editarProduto, produto;
    ProductDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        produto = new Product();
        dbHelper = new ProductDB(ProductsForm.this);

        Intent intent = getIntent();
        editarProduto = (Product) intent.getSerializableExtra("produto-escolhido");

        edit_ProductName = (EditText) findViewById(R.id.edit_ProductName);
        edit_ProductDescription = (EditText) findViewById(R.id.edit_ProductDescription);
        edit_ProductQuantity = (EditText) findViewById(R.id.edit_ProductQuantity);

        btn_Polimorf = (Button) findViewById(R.id.btn_Polimorf);

        if(editarProduto != null){
            btn_Polimorf.setText("Modificar");
            edit_ProductName.setText(editarProduto.getNomeProduto());
            edit_ProductDescription.setText(editarProduto.getDescricao());
            edit_ProductQuantity.setText(editarProduto.getQuantidade() + "");

            produto.setId(editarProduto.getId());
        } else {
            btn_Polimorf.setText("Cadastrar");
        }

        btn_Polimorf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProductsForm.this, MainActivity.class);
                produto.setNomeProduto(edit_ProductName.getText().toString());
                produto.setDescricao(edit_ProductDescription.getText().toString());
                produto.setQuantidade(Integer.parseInt(edit_ProductQuantity.getText().toString()));

                if(btn_Polimorf.getText().toString().equals("Cadastrar")){
                    dbHelper.createProduct(produto);
                    dbHelper.close();
                    startActivity(intent);
                } else {
                    dbHelper.updateProduct(produto);
                    dbHelper.close();
                    startActivity(intent);
                }
            }
        });
    }
}