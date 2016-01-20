package com.example.javi.contentmusicprovider;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.javi.contentmusicprovider.base_de_datos.Contrato;
import com.example.javi.contentmusicprovider.cancion.Cancion;
import com.example.javi.contentmusicprovider.disco.Disco;
import com.example.javi.contentmusicprovider.interprete.Interprete;

public class Principal extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Button musicButton;
    private ListView listView;
    Cursor curu;
    private Adaptador adaptador;
    private int progress;
    private android.widget.ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        this.listView = (ListView) findViewById(R.id.listView);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        init();
        final Cursor c = getContentResolver().query(Contrato.TablaCancion.CONTENT_URI, null, null, null, null);

        adaptador = new Adaptador(this, c);
        listView.setAdapter(adaptador);
        getLoaderManager().initLoader(Contrato.TablaCancion.CANCIONES_ID, null, this);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Cancion cancion;
                Cursor cur = (Cursor) adaptador.getItem(position);
                cur.moveToPosition(position);
                    cancion = new Cancion(0, cur.getString(cur.getColumnIndex(Contrato.TablaCancion.TITULO)), cur.getLong(cur.getColumnIndex(Contrato.TablaCancion.IDDISCO)));
                    Log.v("MICANCION", cancion.toString());

/*
                    Cursor curDisco = getContentResolver().query(Contrato.TablaDisco.CONTENT_URI, null, Contrato.TablaDisco.IDDISCO + " = " + cancion.getIdDisco(), null, null);
                    final Disco disco = new Disco();
                    disco.set(curDisco);

                    Cursor curInter = getContentResolver().query(Contrato.TablaInterprete.CONTENT_URI, null, Contrato.TablaInterprete.IDINTERPRETE + " = " + disco.getIdInterprete(), null, null);
                    final Interprete inter = new Interprete(0, curInter.getString(curInter.getColumnIndex(Contrato.TablaInterprete.NOMBRE)),
                           curInter.getLong(curInter.getColumnIndex(Contrato.TablaInterprete.IDINTERPRETE)));

*/
                    final OnDialogoListener odl = new OnDialogoListener() {

                        @Override
                        public void onPreShow(View v) {
                            TextView tvCancion = (TextView) v.findViewById(R.id.tvTitulo);
                            TextView tvInterprete = (TextView) v.findViewById(R.id.tvInterprete);
                            TextView tvDisco = (TextView) v.findViewById(R.id.tvDisco);

                            tvCancion.setText(cancion.getTitulo());
                            //tvInterprete.setText(inter.getNombre());
                            //tvDisco.setText(disco.getNombre());
                        }

                        @Override
                        public void onOkSelected(View v) {
                        }
                    };
                    DialogoDetalles dia = new DialogoDetalles(Principal.this, R.layout.detalles, odl);
                    dia.show();
            }
        });
    }

    private void init() {
        TareaBuscar t = new TareaBuscar();
        t.execute();
    }

    public class TareaBuscar extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Cursor cur = getContentResolver().query(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
            /*
            ContentResolver musicResolver = getContentResolver();
            Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
            */

            if (cur != null && cur.moveToFirst()) {
                //get columns
                int titleColumn = cur.getColumnIndex
                        (MediaStore.Audio.Media.TITLE);
                int idColumn = cur.getColumnIndex
                        (MediaStore.Audio.Media._ID);
                int idAlbumColumn = cur.getColumnIndex
                        (MediaStore.Audio.Media.ALBUM_ID);

                int albumTitleColumn = cur.getColumnIndex(MediaStore.Audio.Media.ALBUM);

                int idArtistColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
                int artistTitleColumn = cur.getColumnIndex(MediaStore.Audio.Media.ARTIST);

                //add songs to list


                //do {
                    // CANCION -----------------------------------------------------------

                    String thisTitle = cur.getString(titleColumn);
                    long thisIdAlbum = cur.getLong(idAlbumColumn);
                    Cancion c = new Cancion(0, thisTitle, thisIdAlbum);

                    if (c.getIdDisco() == 230) {
                        //Siguiente
                    } else {
                        getContentResolver().insert(Contrato.TablaCancion.CONTENT_URI, c.getContentValues());
                    }
                    Log.v("CANCION", c.toString());


                    // DISCO -------------------------------------------------------------

                    String thisAlbumTitle = cur.getString(albumTitleColumn);
                    long thisIdArtist = cur.getLong(idArtistColumn);
                    Disco d = new Disco(0, thisAlbumTitle, thisIdArtist, thisIdAlbum);
                    getContentResolver().insert(Contrato.TablaDisco.CONTENT_URI, d.getContentValues());
                    Log.v("DISCO", d.toString());

                    // INTERPRETE --------------------------------------------------------
                    String thisArtistTitle = cur.getString(artistTitleColumn);
                    Interprete i = new Interprete(0, thisArtistTitle, thisIdArtist);
                    getContentResolver().insert(Contrato.TablaInterprete.CONTENT_URI, i.getContentValues());
                    progress += 1;
                    publishProgress(progress);
                //} while (cur.moveToNext());
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(progress);
        }

        @Override
        protected void onPreExecute() {
            progressBar.setProgress(0);
            progressBar.setMax(100);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Busqueda finalizada", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == Contrato.TablaCancion.CANCIONES_ID) {
            Uri uri = Contrato.TablaCancion.CONTENT_URI;
            return new CursorLoader(this, uri, null, null, null, Contrato.TablaCancion.TITULO + " collate localized asc ");
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == Contrato.TablaCancion.CANCIONES_ID) {
            adaptador.swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == Contrato.TablaCancion.CANCIONES_ID) {
            adaptador.swapCursor(null);
        }
    }
}
