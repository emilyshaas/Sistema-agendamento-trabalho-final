
package ifrs.ibiruba.agendamentoservico.controller;

import ifrs.ibiruba.agendamentoservico.model.Servico;
import ifrs.ibiruba.agendamentoservico.repository.ServicoRepository;

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

public class TelaServicosController {

    @FXML
    private TextField campoPesquisa;

    @FXML
    private TextField campoNome;

    @FXML
    private TextField campoDescricao;

    @FXML
    private TextField campoPreco;

    @FXML
    private TextField campoDuracao;

    @FXML
    private CheckBox campoAtivo;

    @FXML
    private TableView<Servico> tabelaServicos;

    @FXML
    private TableColumn<Servico, String> colunaNome;

    @FXML
    private TableColumn<Servico, String> colunaDescricao;

    @FXML
    private TableColumn<Servico, Double> colunaPreco;

    @FXML
    private TableColumn<Servico, Integer> colunaDuracao;

    @FXML
    private TableColumn<Servico, Boolean> colunaAtivo;

    private final ServicoRepository repository =
            new ServicoRepository();

    private final ObservableList<Servico> listaServicos =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Configura as colunas da tabela
        colunaNome.setCellValueFactory(
                new PropertyValueFactory<>("nome"));

        colunaDescricao.setCellValueFactory(
                new PropertyValueFactory<>("descricao"));

        colunaPreco.setCellValueFactory(
                new PropertyValueFactory<>("preco"));

        colunaDuracao.setCellValueFactory(
                new PropertyValueFactory<>("duracaoMinutos"));

        colunaAtivo.setCellValueFactory(
                new PropertyValueFactory<>("ativo"));

        // Mostra Ativo ou Inativo
        colunaAtivo.setCellFactory(coluna ->
                new TableCell<Servico, Boolean>() {

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

        FilteredList<Servico> listaFiltrada = new FilteredList<>(listaServicos, item -> true);

        campoPesquisa.textProperty().addListener((observacao, antigo, novo) -> {
            String busca = novo == null ? "" : novo.trim().toLowerCase();
            listaFiltrada.setPredicate(item -> busca.isEmpty() || item.getNome().toLowerCase().contains(busca) || (item.getDescricao() != null && item.getDescricao().toLowerCase().contains(busca)) || String.valueOf(item.getPreco()).contains(busca) || String.valueOf(item.getDuracaoMinutos()).contains(busca) || (item.isAtivo() ? "ativo" : "inativo").contains(busca));
        });

        tabelaServicos.setItems(listaFiltrada);

        carregarServicos();

        // Carrega os dados nos campos ao selecionar um serviço
        tabelaServicos.getSelectionModel()
                .selectedItemProperty()
                .addListener((observacao, antigo, selecionado) -> {

                    if (selecionado != null) {

                        campoNome.setText(selecionado.getNome());

                        campoDescricao.setText(
                                selecionado.getDescricao());

                        campoPreco.setText(
                                String.valueOf(selecionado.getPreco()));

                        campoDuracao.setText(
                                String.valueOf(
                                        selecionado.getDuracaoMinutos()));

                        campoAtivo.setSelected(
                                selecionado.isAtivo());
                    }
                });
    }

    // Carregar serviços do banco
    private void carregarServicos() {

        try {
            listaServicos.setAll(repository.listarTodos());

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível carregar os serviços.");

            e.printStackTrace();
        }
    }

    // Salvar ou atualizar serviço
    @FXML
    public void salvarServico() {

        String nome = campoNome.getText().trim();
        String descricao = campoDescricao.getText().trim();
        String textoPreco = campoPreco.getText().trim();
        String textoDuracao = campoDuracao.getText().trim();

        boolean ativo = campoAtivo.isSelected();

        if (nome.isEmpty()
                || textoPreco.isEmpty()
                || textoDuracao.isEmpty()) {

            mostrarAlerta("Atenção",
                    "Preencha o nome, o preço e a duração.");

            return;
        }

        try {

            // Aceita vírgula ou ponto como separador decimal
            double preco = Double.parseDouble(
                    textoPreco.replace(",", "."));

            int duracao = Integer.parseInt(textoDuracao);

            if (preco <= 0 || duracao <= 0) {

                mostrarAlerta("Atenção",
                        "O preço deve ser maior que zero e "
                        + "a duração deve ser maior que zero.");

                return;
            }

            Servico selecionado =
                    tabelaServicos.getSelectionModel()
                            .getSelectedItem();

            if (selecionado == null) {

                // Cadastrar novo serviço
                Servico servico = new Servico(
                        nome,
                        descricao,
                        preco,
                        duracao,
                        ativo
                );

                repository.salvar(servico);

                mostrarAlerta("Sucesso",
                        "Serviço cadastrado com sucesso!");

            } else {

                // Atualizar serviço selecionado
                selecionado.setNome(nome);
                selecionado.setDescricao(descricao);
                selecionado.setPreco(preco);
                selecionado.setDuracaoMinutos(duracao);
                selecionado.setAtivo(ativo);

                repository.atualizar(selecionado);

                mostrarAlerta("Sucesso",
                        "Serviço atualizado com sucesso!");
            }

            carregarServicos();
            limparCampos();

        } catch (NumberFormatException e) {

            mostrarAlerta("Atenção",
                    "Informe um preço e uma duração válidos.");

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível salvar o serviço.");

            e.printStackTrace();
        }
    }

    // Limpar os campos
    @FXML
    public void limparCampos() {

        campoNome.clear();
        campoDescricao.clear();
        campoPreco.clear();
        campoDuracao.clear();

        campoAtivo.setSelected(true);

        tabelaServicos.getSelectionModel()
                .clearSelection();
    }

    // Excluir serviço selecionado
    @FXML
    public void excluirServico() {

        Servico selecionado =
                tabelaServicos.getSelectionModel()
                        .getSelectedItem();

        if (selecionado == null) {

            mostrarAlerta("Atenção",
                    "Selecione um serviço na tabela.");

            return;
        }

        if (!confirmarExclusao("Tem certeza de que deseja excluir o serviço?")) {
            return;
        }

        try {

            repository.excluir(selecionado.getId());

            carregarServicos();
            limparCampos();

            mostrarAlerta("Sucesso",
                    "Serviço excluído com sucesso!");

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
