
package ifrs.ibiruba.agendamentoservico.controller;

import ifrs.ibiruba.agendamentoservico.model.Profissional;
import ifrs.ibiruba.agendamentoservico.repository.ProfissionalRepository;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TelaProfissionaisController {

    @FXML
    private TextField campoPesquisa;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoEspecialidade;

    @FXML
    private TextField campoTelefone;

    @FXML
    private CheckBox campoAtivo;

    @FXML
    private TableView<Profissional> tabelaProfissionais;

    @FXML
    private TableColumn<Profissional, String> colunaNome;

    @FXML
    private TableColumn<Profissional, String> colunaEspecialidade;

    @FXML
    private TableColumn<Profissional, String> colunaTelefone;

    @FXML
    private TableColumn<Profissional, Boolean> colunaAtivo;

    private final ProfissionalRepository repository =
            new ProfissionalRepository();

    private final ObservableList<Profissional> listaProfissionais =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Configura as colunas da tabela
        colunaNome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));

        colunaEspecialidade.setCellValueFactory(
                new PropertyValueFactory<>("especialidade"));

        colunaTelefone.setCellValueFactory(
                new PropertyValueFactory<>("telefone"));

        colunaAtivo.setCellValueFactory(
                new PropertyValueFactory<>("ativo"));

        // Mostra Ativo ou Inativo na tabela
        colunaAtivo.setCellFactory(coluna ->
                new TableCell<Profissional, Boolean>() {

            @Override
            protected void updateItem(Boolean ativo, boolean vazio) {
                super.updateItem(ativo, vazio);

                if (vazio || ativo == null) {
                    setText(null);
                } else {
                    setText(ativo ? "Ativo" : "Inativo");
                }
            }
        });

        FilteredList<Profissional> listaFiltrada = new FilteredList<>(listaProfissionais, item -> true);

        campoPesquisa.textProperty().addListener((observacao, antigo, novo) -> {
            String busca = novo == null ? "" : novo.trim().toLowerCase();
            listaFiltrada.setPredicate(item -> busca.isEmpty() || item.getNome().toLowerCase().contains(busca) || item.getEspecialidade().toLowerCase().contains(busca) || item.getTelefone().toLowerCase().contains(busca) || (item.isAtivo() ? "ativo" : "inativo").contains(busca));
        });

        tabelaProfissionais.setItems(listaFiltrada);

        carregarProfissionais();

        // Carrega os dados nos campos ao selecionar um profissional
        tabelaProfissionais.getSelectionModel()
                .selectedItemProperty()
                .addListener((observacao, antigo, selecionado) -> {

                    if (selecionado != null) {

                        campoNome.setText(selecionado.getNome());

                        campoEspecialidade.setText(
                                selecionado.getEspecialidade());

                        campoTelefone.setText(
                                selecionado.getTelefone());

                        campoAtivo.setSelected(
                                selecionado.isAtivo());
                    }
                });
    }

    // Carregar profissionais do banco
    private void carregarProfissionais() {

        try {
            listaProfissionais.setAll(
                    repository.listarTodos());

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível carregar os profissionais.");

            e.printStackTrace();
        }
    }

    // Salvar ou atualizar profissional
    @FXML
    public void salvarProfissional() {

        String nome = campoNome.getText().trim();

        String especialidade =
                campoEspecialidade.getText().trim();

        String telefone = campoTelefone.getText().trim();

        boolean ativo = campoAtivo.isSelected();

        if (nome.isEmpty()
                || especialidade.isEmpty()
                || telefone.isEmpty()) {

            mostrarAlerta("Atenção",
                    "Preencha todos os campos obrigatórios.");

            return;
        }

        String telefoneNumeros = telefone.replaceAll("\\D", "");
        if (telefoneNumeros.length() < 8 || telefoneNumeros.length() > 15) {
            mostrarAlerta("Atenção",
                    "Informe um telefone válido.");
            return;
        }

        try {

            Profissional selecionado =
                    tabelaProfissionais.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                // Cadastrar novo profissional
                Profissional profissional =
                        new Profissional(
                                nome,
                                especialidade,
                                telefone,
                                ativo
                        );

                repository.salvar(profissional);

                mostrarAlerta("Sucesso",
                        "Profissional cadastrado com sucesso!");

            } else {

                // Atualizar profissional selecionado
                selecionado.setNome(nome);
                selecionado.setEspecialidade(especialidade);
                selecionado.setTelefone(telefone);
                selecionado.setAtivo(ativo);

                repository.atualizar(selecionado);

                mostrarAlerta("Sucesso",
                        "Profissional atualizado com sucesso!");
            }

            carregarProfissionais();
            limparCampos();

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível salvar o profissional.");

            e.printStackTrace();
        }
    }

    // Limpar os campos
    @FXML
    public void limparCampos() {

        campoNome.clear();
        campoEspecialidade.clear();
        campoTelefone.clear();

        campoAtivo.setSelected(true);

        tabelaProfissionais.getSelectionModel()
                .clearSelection();
    }

    // Excluir profissional selecionado
    @FXML
    public void excluirProfissional() {

        Profissional selecionado =
                tabelaProfissionais.getSelectionModel()
                        .getSelectedItem();

        if (selecionado == null) {

            mostrarAlerta("Atenção",
                    "Selecione um profissional na tabela.");

            return;
        }

        if (!confirmarExclusao("Tem certeza de que deseja excluir o profissional?")) {
            return;
        }

        try {

            repository.excluir(selecionado.getId());

            carregarProfissionais();
            limparCampos();

            mostrarAlerta("Sucesso",
                    "Profissional excluído com sucesso!");

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível excluir. Verifique se este registro está vinculado a um agendamento.");

            e.printStackTrace();
        }
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
