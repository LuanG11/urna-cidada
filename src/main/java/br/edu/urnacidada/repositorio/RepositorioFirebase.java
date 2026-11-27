package br.edu.urnacidada.repositorio;

import br.edu.urnacidada.modelo.Voto;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Usa a variável GOOGLE_APPLICATION_CREDENTIALS para localizar a chave JSON. */
public class RepositorioFirebase implements RepositorioVotos {
    private final Firestore firestore;

    public RepositorioFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();
            FirebaseApp.initializeApp(options);
        }
        firestore = FirestoreClient.getFirestore();
    }

    @Override
    public void salvar(Voto voto) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("cargo", voto.getCargo());
        dados.put("numero", voto.getNumero());
        dados.put("horario", voto.getHorario().toString());
        dados.put("data", voto.getHorario().toLocalDate().toString());
        firestore.collection("votos").add(dados);
    }

    @Override
    public List<Voto> listarPorData(LocalDate data) {
        List<Voto> votos = new ArrayList<>();
        try {
            // Lê também registros antigos, criados antes do campo "data" existir.
            var documentos = firestore.collection("votos").get().get().getDocuments();
            for (var documento : documentos) {
                String cargo = documento.getString("cargo");
                Long numero = documento.getLong("numero");
                String dataRegistro = documento.getString("data");
                String horario = documento.getString("horario");
                boolean mesmaData = data.toString().equals(dataRegistro) ||
                        (horario != null && horario.startsWith(data.toString()));
                if (mesmaData && cargo != null && numero != null)
                    votos.add(new Voto(cargo, numero.intValue()));
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Não foi possível consultar o Firebase: " + ex.getMessage(), ex);
        }
        return votos;
    }
}
