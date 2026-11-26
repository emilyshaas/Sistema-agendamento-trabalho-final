
package ifrs.ibiruba.agendamentoservico.controller;

import ifrs.ibiruba.agendamentoservico.model.Agendamento;
import ifrs.ibiruba.agendamentoservico.model.Cliente;
import ifrs.ibiruba.agendamentoservico.model.Profissional;
import ifrs.ibiruba.agendamentoservico.model.Servico;

import ifrs.ibiruba.agendamentoservico.repository.AgendamentoRepository;
import ifrs.ibiruba.agendamentoservico.repository.ClienteRepository;
import ifrs.ibiruba.agendamentoservico.repository.ProfissionalRepository;
import ifrs.ibiruba.agendamentoservico.repository.ServicoRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.stream.Collectors;

public class TelaAgendamentosController {

    @FXML
    private TextField campoPesquisa;

    @FXML
    private ComboBox<Cliente> comboCliente;

    @FXML
    private ComboBox<Profissional> comboProfissional;

    @FXML
    private ComboBox<Servico> comboServico;

    @FXML
    private DatePicker campoData;

    @FXML
    private TextField campoHorario;

    @FXML
    private ComboBox<String> comboSituacao;

    @FXML
    private TableView<Agendamento> tabelaAgendamentos;

    @FXML
    private TableColumn<Agendamento, LocalDate> colunaData;

    @FXML
    private TableColumn<Agendamento, LocalTime> colunaHorario;

    @FXML
    private TableColumn<Agendamento, Cliente> colunaCliente;

    @FXML
    private TableColumn<Agendamento, Servico> colunaServico;

    @FXML
    private TableColumn<Agendamento, Profissional> colunaProfissional;

    @FXML
    private TableColumn<Agendamento, String> colunaSituacao;

    private final AgendamentoRepository agendamentoRepository =
            new AgendamentoRepository();

    private final ClienteRepository clienteRepository =
            new ClienteRepository();

    private final ProfissionalRepository profissionalRepository =
            new ProfissionalRepository();

    private final ServicoRepository servicoRepository =
            new ServicoRepository();

    private final ObservableList<Agendamento> listaAgendamentos =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        // Configura as colunas da tabela
        colunaData.setCellValueFactory(
                new PropertyValueFactory<>("data"));

        colunaHorario.setCellValueFactory(
                new PropertyValueFactory<>("horario"));

        colunaCliente.setCellValueFactory(
                new PropertyValueFactory<>("cliente"));

        colunaServico.setCellValueFactory(
                new PropertyValueFactory<>("servico"));

        colunaProfissional.setCellValueFactory(
                new PropertyValueFactory<>("profissional"));

        colunaSituacao.setCellValueFactory(
                new PropertyValueFactory<>("situacao"));

        FilteredList<Agendamento> listaFiltrada = new FilteredList<>(listaAgendamentos, item -> true);

        campoPesquisa.textProperty().addListener((observacao, antigo, novo) -> {
            String busca = novo == null ? "" : novo.trim().toLowerCase();
            listaFiltrada.setPredicate(item -> busca.isEmpty() || item.getCliente().getNome().toLowerCase().contains(busca) || item.getProfissional().getNome().toLowerCase().contains(busca) || item.getServico().getNome().toLowerCase().contains(busca) || item.getSituacao().toLowerCase().contains(busca) || item.getData().toString().contains(busca) || item.getHorario().toString().contains(busca));
        });

        tabelaAgendamentos.setItems(listaFiltrada);

        // Opções de situação
        comboSituacao.setItems(
                FXCollections.observableArrayList(
                        "Pendente",
                        "Confirmado",
                        "Concluído",
                        "Cancelado"
                )
        );

        comboSituacao.setValue("Pendente");

        carregarDados();

        // Carrega os dados ao selecionar um agendamento
        tabelaAgendamentos.getSelectionModel()
                .selectedItemProperty()
                .addListener((observacao, antigo, selecionado) -> {

                    if (selecionado != null) {

                        comboCliente.setValue(
                                selecionado.getCliente());

                        comboProfissional.setValue(
                                selecionado.getProfissional());

                        comboServico.setValue(
                                selecionado.getServico());

                        campoData.setValue(
                                selecionado.getData());

                        campoHorario.setText(
                                selecionado.getHorario().format(
                                        DateTimeFormatter.ofPattern("HH:mm")));

                        comboSituacao.setValue(
                                selecionado.getSituacao());
                    }
                });
    }

    // Carregar clientes, profissionais, serviços e agendamentos
    // Carregar clientes, profissionais, serviços e agendamentos
private void carregarDados() {

    try {

        comboCliente.setItems(
                FXCollections.observableArrayList(
                        clienteRepository.listarTodos()));

        comboProfissional.setItems(
                FXCollections.observableArrayList(
                        profissionalRepository.listarTodos()
                                .stream()
                                .filter(Profissional::isAtivo)
                                .collect(Collectors.toList())));

        comboServico.setItems(
                FXCollections.observableArrayList(
                        servicoRepository.listarTodos()
                                .stream()
                                .filter(Servico::isAtivo)
                                .collect(Collectors.toList())));

        listaAgendamentos.setAll(
                agendamentoRepository.listarTodos());

    } catch (Exception e) {

        mostrarAlerta("Erro",
                "Não foi possível carregar os dados.");

        e.printStackTrace();
    }
}

    // Salvar ou atualizar agendamento
    @FXML
    public void salvarAgendamento() {

        Cliente cliente = comboCliente.getValue();
        Profissional profissional = comboProfissional.getValue();
        Servico servico = comboServico.getValue();

        LocalDate data = campoData.getValue();

        String horarioTexto = campoHorario.getText().trim();

        String situacao = comboSituacao.getValue();

        if (cliente == null
                || profissional == null
                || servico == null
                || data == null
                || horarioTexto.isEmpty()
                || situacao == null) {

            mostrarAlerta("Atenção",
                    "Preencha todos os campos do agendamento.");

            return;
        }

        try {

            LocalTime horario = LocalTime.parse(
                    horarioTexto,
                    DateTimeFormatter.ofPattern("HH:mm"));

            if (data.isBefore(LocalDate.now())
                    || (data.equals(LocalDate.now())
                    && horario.isBefore(LocalTime.now()))) {
                mostrarAlerta("Atenção",
                        "Não é possível marcar um agendamento no passado.");
                return;
            }

            Agendamento selecionado =
                    tabelaAgendamentos.getSelectionModel()
                            .getSelectedItem();

            if (existeConflitoDeHorario(data, horario, profissional,
                    servico, selecionado)) {
                mostrarAlerta("Atenção",
                        "Esse profissional já possui um agendamento "
                        + "nesse intervalo de horário.");
                return;
            }

            if (selecionado == null) {

                // Cadastrar novo agendamento
                Agendamento agendamento = new Agendamento(
                        data,
                        horario,
                        situacao,
                        cliente,
                        profissional,
                        servico
                );

                agendamentoRepository.salvar(agendamento);

                mostrarAlerta("Sucesso",
                        "Agendamento cadastrado com sucesso!");

            } else {

                // Atualizar agendamento selecionado
                selecionado.setData(data);
                selecionado.setHorario(horario);
                selecionado.setSituacao(situacao);
                selecionado.setCliente(cliente);
                selecionado.setProfissional(profissional);
                selecionado.setServico(servico);

                agendamentoRepository.atualizar(selecionado);

                mostrarAlerta("Sucesso",
                        "Agendamento atualizado com sucesso!");
            }

            carregarDados();
            limparCampos();

        } catch (DateTimeParseException e) {

            mostrarAlerta("Atenção",
                    "Informe um horário válido no formato HH:mm.");

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível salvar o agendamento.");

            e.printStackTrace();
        }
    }

    // Verifica se o profissional já possui outro agendamento no intervalo
    private boolean existeConflitoDeHorario(
            LocalDate data,
            LocalTime horario,
            Profissional profissional,
            Servico servico,
            Agendamento selecionado) {

        int inicioNovo = horario.getHour() * 60 + horario.getMinute();
        int fimNovo = inicioNovo + servico.getDuracaoMinutos();

        for (Agendamento existente : listaAgendamentos) {

            if (selecionado != null
                    && existente.getId().equals(selecionado.getId())) {
                continue;
            }

            if (!existente.getData().equals(data)
                    || !existente.getProfissional().getId()
                            .equals(profissional.getId())) {
                continue;
            }

            if ("Cancelado".equalsIgnoreCase(existente.getSituacao())
                    || "Concluído".equalsIgnoreCase(existente.getSituacao())) {
                continue;
            }

            int inicioExistente = existente.getHorario().getHour() * 60
                    + existente.getHorario().getMinute();

            int fimExistente = inicioExistente
                    + existente.getServico().getDuracaoMinutos();

            if (inicioNovo < fimExistente
                    && inicioExistente < fimNovo) {
                return true;
            }
        }

        return false;
    }

    // Limpar os campos
    @FXML
    public void limparCampos() {

        comboCliente.setValue(null);
        comboProfissional.setValue(null);
        comboServico.setValue(null);

        campoData.setValue(null);
        campoHorario.clear();

        comboSituacao.setValue("Pendente");

        tabelaAgendamentos.getSelectionModel()
                .clearSelection();
    }

    // Excluir agendamento selecionado
    @FXML
    public void excluirAgendamento() {

        Agendamento selecionado =
                tabelaAgendamentos.getSelectionModel()
                        .getSelectedItem();

        if (selecionado == null) {

            mostrarAlerta("Atenção",
                    "Selecione um agendamento na tabela.");

            return;
        }

        if (!confirmarExclusao("Tem certeza de que deseja excluir o agendamento?")) {
            return;
        }

        try {

            agendamentoRepository.excluir(selecionado.getId());

            carregarDados();
            limparCampos();

            mostrarAlerta("Sucesso",
                    "Agendamento excluído com sucesso!");

        } catch (Exception e) {

            mostrarAlerta("Erro",
                    "Não foi possível excluir o agendamento.");

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

            Stage stage = (Stage) tabelaAgendamentos
                    .getScene().getWindow();

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
