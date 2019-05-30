package jakhar.aseem.diva;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsecureDataStorage1Activity extends AppCompatActivity {
    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_insecure_data_storage1);
    }

    public void saveCredentials(View view) {
        Editor spedit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        EditText pwd = (EditText) findViewById(R.id.ids1Pwd);
        spedit.putString("user", ((EditText) findViewById(R.id.ids1Usr)).getText().toString());
        spedit.putString("password", pwd.getText().toString());
        spedit.commit();
        Toast.makeText(this, "3rd party credentials saved successfully!", 0).show();
    }
}
