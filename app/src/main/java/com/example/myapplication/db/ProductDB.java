package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import com.example.myapplication.model.Product;
import java.util.ArrayList;

public class ProductDB extends SQLiteOpenHelper {

    private static final String DATABASE = "dbprodutos";
    private static final int VERSION = 1;

    public ProductDB (Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String product = "CREATE TABLE products(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, productName TEXT NOT NULL, description TEXT NOT NULL, quantity INTEGER);";
        db.execSQL(product);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String product = "DROP TABLE IF EXISTS products";
        db.execSQL(product);
    }

    public void createProduct(Product produto){
        ContentValues values = new ContentValues();

        values.put("productName", produto.getNomeProduto());
        values.put("description", produto.getDescricao());
        values.put("quantity", produto.getQuantidade());

        getWritableDatabase().insert("products", null, values);
    }

    public void updateProduct(Product produto){
        ContentValues values = new ContentValues();

        values.put("productName", produto.getNomeProduto());
        values.put("description", produto.getDescricao());
        values.put("quantity", produto.getQuantidade());

        String [] args = {produto.getId().toString()};
        getWritableDatabase().update("products",  values, "id=?", args);
    }

    public void deleteProduct(Product produto){
        String [] args = {produto.getId().toString()};
        getWritableDatabase().delete("products", "id=?", args);
    }

    public ArrayList<Product> getLista() {
        String [] columns = {"id", "productName", "description", "quantity"};
        Cursor cursor = getWritableDatabase().query("products", columns, null, null, null, null, null, null);
        ArrayList<Product> produtos = new ArrayList<Product>();

        while (cursor.moveToNext()) {
            Product produto = new Product();
            produto.setId(cursor.getLong(0));
            produto.setNomeProduto(cursor.getString(1));
            produto.setDescricao(cursor.getString(2));
            produto.setQuantidade(cursor.getInt(3));

            produtos.add(produto);
        }
        return produtos;
    }


}
