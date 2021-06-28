package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola artisti connessi \n");
    	
    	String ruolo = boxRuolo.getValue();
    	if(ruolo == null) {
        	txtResult.appendText("Devi scegliere un ruolo");
        	return;
    	}
    	
    	for(Arco a: model.getConnessi(ruolo)) {
    		txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso \n");
    	
    	String sId = txtArtista.getText();
    	int id = 0;
    	try {
    		id = Integer.parseInt(sId);
    	} catch(NumberFormatException n) {
    		txtResult.appendText("Devi inserire un numero identificativo per artista!");
    		return;
    	}
    	
    	this.model.camminoPiuLungo(id);
    	txtResult.appendText("Percorso più lungo con numero di esposizioni pari a: " + model.getNumEsp()+"\n");
    	for(Artist a: this.model.camminoPiuLungo(id)) {
    		txtResult.appendText(a.getName()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo");
    	String ruolo = boxRuolo.getValue();
    	if(ruolo == null) {
        	txtResult.appendText("Devi scegliere un ruolo");
        	return;
    	}
    	this.model.creaGrafo(ruolo);
    	txtResult.appendText("GRAFO CREATO! \n");
    	txtResult.appendText("# VERTICI: " + this.model.nVertici()+"\n");
    	txtResult.appendText("# ARCHI: " + this.model.nArchi());
    }

    public void setModel(Model model) {
    	this.model = model;
    	boxRuolo.getItems().addAll(model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

    }
}
