package com.rel3.lixoconsciente.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rel3.lixoconsciente.adapter.ListaCepAdapter;
import com.rel3.lixoconsciente.dbhelper.DBHelper;
import com.rel3.lixoconsciente.R;
import com.rel3.lixoconsciente.model.Endereco;
import com.rel3.lixoconsciente.tasks.EnderecoTask;

import java.util.ArrayList;
import java.util.List;


public class BuscaActivity extends AppCompatActivity {


    private TextView edEndereco;
    private DBHelper db;
    private ListView listView;
    private Endereco endereco = new Endereco();
    private List<String> lista;
    private ListaCepAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);

        edEndereco = (TextView)findViewById(R.id.edEndereco);
        edEndereco.setMaxWidth(edEndereco.getWidth());

        listView = (ListView) findViewById(R.id.listView);

        lista = new ArrayList<>();
        adapter = new ListaCepAdapter(this, lista);
        listView.setAdapter(adapter);

       atualizarLista();
    }



    private void atualizarLista(){

        db = new DBHelper(BuscaActivity.this);
        lista.clear();

//        if(lista.size() != 0) {
        lista.addAll(db.listarTudo());
        //adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lista);
        adapter.notifyDataSetChanged();

        //listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edEndereco.setText("");
                int codigo = position + 1;
                endereco.setId(codigo);
                endereco.setEndereco((String)parent.getItemAtPosition(position));
                edEndereco.setText(endereco.getEndereco());
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BuscaActivity.this);
                builder.setMessage(R.string.delete)
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.deletarCep(endereco.getId());
                                edEndereco.setText("");
                                Toast.makeText(getApplicationContext(), "Endereço deletado!", Toast.LENGTH_SHORT).show();
                                atualizarLista();
                            }
                        })
                        .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog d = builder.create();
                d.setTitle("Excluir endereço");
                d.show();
                edEndereco.setText("");
                atualizarLista();
                return false;
            }
        });

    }

    public void buscar(View v){
        EnderecoTask eTask = new EnderecoTask(this, BuscaActivity.this);
        String url = "http://maps.google.com/maps/api/geocode/json?address="+edEndereco.getText().toString()+"&sensor=false";
        url = url.replace(" ", "%20");
        System.out.println(url);
        eTask.execute(url);
        //System.out.println(edEndereco.getText().toString());
        //Toast.makeText(this, "Buscando CEP", Toast.LENGTH_LONG).show();
    }

    public void cadastrar(View v){
        endereco.setEndereco(edEndereco.getText().toString());
        db.cadastrarCep(endereco);
        edEndereco.setText("");
        Toast.makeText(getApplicationContext(), "Endereço adicionado aos favoritos!", Toast.LENGTH_LONG).show();
        atualizarLista();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.acoes_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//        switch (item.getItemId()){
//            case R.id.Add:
//                endereco.setEndereco(edEndereco.getText().toString());
//                db.cadastrarCep(endereco);
//                edEndereco.setText("");
//                Toast.makeText(getApplicationContext(), "Endereço adicionado aos favoritos!", Toast.LENGTH_LONG).show();
//                atualizarLista();
//                return true;
//            //case R.id.Delete:
//
////                AlertDialog.Builder builder = new AlertDialog.Builder(this);
////                builder.setMessage(R.string.delete)
////                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener(){
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                db.deletarCep(cep.getId());
////                                Toast.makeText(getApplicationContext(), "CEP deletado!", Toast.LENGTH_SHORT).show();
////                                atualizarLista();
////                            }
////                        })
////                        .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////
////                            }
////                        });
////
////                AlertDialog d = builder.create();
////                d.setTitle("Deseja excluir o cep?");
////                d.show();
////                atualizarLista();
//                //return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
