package br.com.faculdade;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class FormularioCadastroController {

    @FXML
    private TextField txtCpf;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtEndereco;

    @FXML
    private ComboBox<String> comboEstado;

    @FXML
    private ComboBox<String> comboCargo;

    @FXML
    private Button btnImprimir;

    @FXML
    private void initialize() {
        ObservableList<String> estados = FXCollections.observableArrayList(
                "Acre",
                "Alagoas",
                "Amapá",
                "Amazonas",
                "Bahia",
                "Ceará",
                "Distrito Federal",
                "Espírito Santo",
                "Goiás",
                "Maranhão",
                "Minas Gerais",
                "Pará",
                "Paraíba",
                "Paraná",
                "Pernambuco",
                "Piauí",
                "Rio de Janeiro",
                "Rio Grande do Norte",
                "Rio Grande do Sul",
                "Rondônia",
                "Roraima",
                "Santa Catarina",
                "São Paulo",
                "Sergipe",
                "Tocantins"
        );

        ObservableList<String> cargos = FXCollections.observableArrayList(
                "Analista de Sistemas",
                "Assistente Administrativo",
                "Desenvolvedor",
                "Gerente de Marketing",
                "Gerente de Projetos",
                "Professor",
                "Suporte Técnico"
        );

        comboEstado.setItems(estados);
        comboCargo.setItems(cargos);

        comboEstado.getSelectionModel().select("Bahia");
        comboCargo.getSelectionModel().select("Gerente de Marketing");

        // Evento do botão utilizando expressão lambda, conforme solicitado.
        btnImprimir.setOnAction(event -> mostrarDados());
    }

    private void mostrarDados() {
        String cpf = txtCpf.getText();
        String nome = txtNome.getText();
        String endereco = txtEndereco.getText();
        String estado = comboEstado.getValue();
        String cargo = comboCargo.getValue();

        if (cpf == null || cpf.trim().isEmpty()
                || nome == null || nome.trim().isEmpty()
                || endereco == null || endereco.trim().isEmpty()
                || estado == null || cargo == null) {

            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atenção");
            alerta.setHeaderText(null);
            alerta.setContentText("Preencha todos os campos do formulário.");
            alerta.showAndWait();
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Mensagem");
        alerta.setHeaderText(null);
        alerta.setContentText(
                "Cpf: " + cpf
                + "\nNome: " + nome
                + "\nEndereço: " + endereco
                + "\nEstado: " + estado
                + "\nCargo: " + cargo
        );
        alerta.showAndWait();
    }
}
