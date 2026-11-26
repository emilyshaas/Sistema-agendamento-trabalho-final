
package ifrs.ibiruba.agendamentoservico.controller;

import ifrs.ibiruba.agendamentoservico.model.Cliente;
import ifrs.ibiruba.agendamentoservico.repository.ClienteRepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class TelaClientesController {

    @FXML
    private TextField campoPesquisa;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoTelefone;

    @FXML
    private TextField campoEmail;

    @FXML
    private TableView<Cliente> tabelaClientes;

    @FXML
    private TableColumn<Cliente, String> colunaNome;

    @FXML
    private TableColumn<Cliente, String> colunaTelefone;

    @FXML
    private TableColumn<Cliente, String> colunaEmail;

    private final ClienteRepository repository =
            new ClienteRepository();

    private final ObservableList<Cliente> listaClientes =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Configura as colunas da tabela
        colunaNome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));

        colunaTelefone.setCellValueFactory(
                new PropertyValueFactory<>("telefone"));

        colunaEmail.setCellValueFactory(
                new PropertyValueFactory<>("email"));

        // Conecta a lista à tabela
        FilteredList<Cliente> listaFiltrada = new FilteredList<>(listaClientes, item -> true);

        campoPesquisa.textProperty().addListener((observacao, antigo, novo) -> {
            String busca = novo == null ? "" : novo.trim().toLowerCase();
            listaFiltrada.setPredicate(item -> busca.isEmpty() || item.getNome().toLowerCase().contains(busca) || item.getTelefone().toLowerCase().contains(busca) || (item.getEmail() != null && item.getEmail().toLowerCase().contains(busca)));
        });

        tabelaClientes.setItems(listaFiltrada);

        // Carrega os clientes cadastrados
        carregarClientes();

        // Carrega os dados nos campos ao selecionar um cliente
        tabelaClientes.getSelectionModel()
                .selectedItemProperty()
                .addListener((observacao, antigo, selecionado) -> {

                    if (selecionado != null) {
                        campoNome.setText(selecionado.getNome());
                        campoTelefone.setText(selecionado.getTelefone());
                        campoEmail.setText(selecionado.getEmail());
                    }
                });
    }

    // Carregar clientes do banco de dados
    private void carregarClientes() {

        try {
            listaClientes.setAll(repository.listarTodos());

        } catch (Exception e) {
            mostrarAlerta("Erro",
                    "Não foi possível carregar os clientes.");
            e.printStackTrace();
        }
    }

    // Salvar ou atualizar cliente
    @FXML
    public void salvarCliente() {

        String nome = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String email = campoEmail.getText().trim();

        if (nome.isEmpty() || telefone.isEmpty()) {
            mostrarAlerta("Atenção",
                    "Preencha o nome e o telefone.");
            return;
        }

        String telefoneNumeros = telefone.replaceAll("\\D", "");
        if (telefoneNumeros.length() < 8 || telefoneNumeros.length() > 15) {
            mostrarAlerta("Atenção",
                    "Informe um telefone válido.");
            return;
        }

        if (!email.isEmpty()
                && !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta("Atenção",
                    "Informe um endereço de e-mail válido.");
            return;
        }

        try {

            Cliente selecionado =
                    tabelaClientes.getSelectionModel().getSelectedItem();

            if (selecionado == null) {

                // Cadastra um novo cliente
                Cliente cliente =
                        new Cliente(nome, telefone, email);

                repository.salvar(cliente);

                mostrarAlerta("Sucesso",
                        "Cliente cadastrado com sucesso!");

            } else {

                // Atualiza o cliente selecionado
                selecionado.setNome(nome);
                selecionado.setTelefone(telefone);
                selecionado.setEmail(email);

                repository.atualizar(selecionado);

                mostrarAlerta("Sucesso",
                        "Cliente atualizado com sucesso!");
            }

            carregarClientes();
            limparCampos();

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível salvar o cliente.");

            e.printStackTrace();
        }
    }

    // Limpar os campos
    @FXML
    public void limparCampos() {

        campoNome.clear();
        campoTelefone.clear();
        campoEmail.clear();

        tabelaClientes.getSelectionModel().clearSelection();
    }

    // Excluir cliente selecionado
    @FXML
    public void excluirCliente() {

        Cliente selecionado =
                tabelaClientes.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarAlerta("Atenção",
                    "Selecione um cliente na tabela.");
            return;
        }

        if (!confirmarExclusao("Tem certeza de que deseja excluir o cliente?")) {
            return;
        }

        try {

            repository.excluir(selecionado.getId());

            carregarClientes();
            limparCampos();

            mostrarAlerta("Sucesso",
                    "Cliente excluído com sucesso!");

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível excluir. Verifique se este registro está vinculado a um agendamento.");

            e.printStackTrace();
        }
    }

    // Exibir mensagens
    private void mostrarAlerta(String titulo, String mensagem) {

        Alert.AlertType tipo = Alert.AlertType.INFORMATION;

        if ("Erro".equalsIgnoreCase(titulo)) {
            tipo = Alert.AlertType.ERROR;
        } else if ("Atenção".equalsIgnoreCase(titulo)) {
            tipo = Alert.AlertType.WARNING;
        }

        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }
    
    // Voltar para a tela principal
@FXML
public void voltarInicio() {

    try {
        Parent tela = FXMLLoader.load(
                getClass().getResource(
                        "/ifrs/ibiruba/agendamentoservico/telaPrincipal.fxml"
                )
        );

        Stage stage = (Stage) campoNome.getScene().getWindow();

        stage.getScene().setRoot(tela);

    } catch (IOException | RuntimeException e) {

        mostrarAlerta("Erro",
                "Não foi possível voltar à página inicial.");

        e.printStackTrace();
    }
}


    // Solicita confirmação antes de excluir um registro
    private boolean confirmarExclusao(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar exclusão");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);

        return alerta.showAndWait().orElse(ButtonType.CANCEL)
                == ButtonType.OK;
    }
}
