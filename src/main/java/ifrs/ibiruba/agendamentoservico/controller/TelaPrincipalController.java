
package ifrs.ibiruba.agendamentoservico.controller;

import ifrs.ibiruba.agendamentoservico.model.Agendamento;
import ifrs.ibiruba.agendamentoservico.repository.AgendamentoRepository;
import ifrs.ibiruba.agendamentoservico.repository.ClienteRepository;
import ifrs.ibiruba.agendamentoservico.repository.ProfissionalRepository;
import ifrs.ibiruba.agendamentoservico.repository.ServicoRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class TelaPrincipalController {

    @FXML
    private Label labelData;

    @FXML
    private Label labelAgendamentos;

    @FXML
    private Label labelClientes;

    @FXML
    private Label labelProfissionais;

    @FXML
    private Label labelServicos;

    @FXML
    private TableView<AgendamentoResumo> tabelaAgendamentos;

    @FXML
    private TableColumn<AgendamentoResumo, String> colunaHorario;

    @FXML
    private TableColumn<AgendamentoResumo, String> colunaCliente;

    @FXML
    private TableColumn<AgendamentoResumo, String> colunaServico;

    @FXML
    private TableColumn<AgendamentoResumo, String> colunaProfissional;

    @FXML
    private TableColumn<AgendamentoResumo, String> colunaSituacao;

    private final AgendamentoRepository agendamentoRepository =
            new AgendamentoRepository();

    private final ClienteRepository clienteRepository =
            new ClienteRepository();

    private final ProfissionalRepository profissionalRepository =
            new ProfissionalRepository();

    private final ServicoRepository servicoRepository =
            new ServicoRepository();

    private final ObservableList<AgendamentoResumo> listaAgendamentos =
            FXCollections.observableArrayList();

    // Propriedade observável usada no binding do total de agendamentos de hoje
    private final IntegerProperty quantidadeAgendamentosHoje =
            new SimpleIntegerProperty(0);

    @FXML
    public void initialize() {

        // Configura as colunas da tabela
        colunaHorario.setCellValueFactory(dados ->
                new ReadOnlyStringWrapper(
                        dados.getValue().getHorario()));

        colunaCliente.setCellValueFactory(dados ->
                new ReadOnlyStringWrapper(
                        dados.getValue().getCliente()));

        colunaServico.setCellValueFactory(dados ->
                new ReadOnlyStringWrapper(
                        dados.getValue().getServico()));

        colunaProfissional.setCellValueFactory(dados ->
                new ReadOnlyStringWrapper(
                        dados.getValue().getProfissional()));

        colunaSituacao.setCellValueFactory(dados ->
                new ReadOnlyStringWrapper(
                        dados.getValue().getSituacao()));

        // Conecta a lista à tabela
        tabelaAgendamentos.setItems(listaAgendamentos);

        // Binding: o texto do painel acompanha a propriedade observável
        labelAgendamentos.textProperty().bind(
                quantidadeAgendamentosHoje.asString());

        // Exibe a data atual
        DateTimeFormatter formato =
                DateTimeFormatter.ofPattern("dd/MM/yyyy");

        labelData.setText(
                LocalDate.now().format(formato));

        // Carrega as informações do banco
        carregarPainel();
    }

    // Carregar informações do painel
    private void carregarPainel() {

        try {

            // Conta os registros cadastrados
            labelClientes.setText(
                    String.valueOf(
                            clienteRepository.listarTodos().size()));

            labelProfissionais.setText(
                    String.valueOf(
                            profissionalRepository.listarTodos().size()));

            labelServicos.setText(
                    String.valueOf(
                            servicoRepository.listarTodos().size()));

            // Busca todos os agendamentos
            List<Agendamento> agendamentos =
                    agendamentoRepository.listarTodos();

            LocalDate hoje = LocalDate.now();

            // Conta somente os agendamentos de hoje
            long totalHoje = agendamentos.stream()
                    .filter(a -> a.getData().equals(hoje))
                    .count();

            quantidadeAgendamentosHoje.set((int) totalHoje);

            // Separa os próximos agendamentos
            List<AgendamentoResumo> proximos =
                    agendamentos.stream()
                    .filter(a -> !a.getData().isBefore(hoje))
                    .map(a -> new AgendamentoResumo(
                            a.getHorario().format(
                                    DateTimeFormatter.ofPattern("HH:mm")),
                            a.getCliente().getNome(),
                            a.getServico().getNome(),
                            a.getProfissional().getNome(),
                            a.getSituacao()
                    ))
                    .collect(Collectors.toList());

            // Atualiza a tabela
            listaAgendamentos.setAll(proximos);

        } catch (Exception e) {

            exibirMensagem(
                    "Não foi possível carregar os dados do painel."
            );

            e.printStackTrace();
        }
    }

    // Métodos do menu lateral

    @FXML
    private void mostrarInicio() {
        carregarPainel();
    }

    @FXML
    private void mostrarClientes() {

        abrirTela("telaClientes.fxml",
                "Não foi possível abrir a tela de clientes.");
    }

    @FXML
    private void mostrarProfissionais() {

        abrirTela("telaProfissionais.fxml",
                "Não foi possível abrir a tela de profissionais.");
    }

    @FXML
    private void mostrarServicos() {

        abrirTela("telaServicos.fxml",
                "Não foi possível abrir a tela de serviços.");
    }

    @FXML
    private void mostrarAgendamentos() {

        abrirTela("telaAgendamentos.fxml",
                "Não foi possível abrir a tela de agendamentos.");
    }

    @FXML
    private void mostrarRelatorios() {

        exibirMensagem("Tela de relatórios será implementada.");
    }

    @FXML
    private void novoAgendamento() {

        mostrarAgendamentos();
    }

    // Método para abrir outras telas
    private void abrirTela(String arquivo, String mensagemErro) {

        try {

            Parent tela = FXMLLoader.load(
                    getClass().getResource(
                            "/ifrs/ibiruba/agendamentoservico/" + arquivo
                    )
            );

            Stage stage = (Stage) labelData.getScene().getWindow();

            stage.getScene().setRoot(tela);

        } catch (IOException | RuntimeException e) {

            exibirMensagem(mensagemErro);

            e.printStackTrace();
        }
    }

    // Método auxiliar para exibir mensagens
    private void exibirMensagem(String mensagem) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle("Agendamento Serviço");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);

        alerta.showAndWait();
    }

    // Classe auxiliar para representar os dados da tabela
    public static class AgendamentoResumo {

        private final String horario;
        private final String cliente;
        private final String servico;
        private final String profissional;
        private final String situacao;

        public AgendamentoResumo(
                String horario,
                String cliente,
                String servico,
                String profissional,
                String situacao) {

            this.horario = horario;
            this.cliente = cliente;
            this.servico = servico;
            this.profissional = profissional;
            this.situacao = situacao;
        }

        public String getHorario() {
            return horario;
        }

        public String getCliente() {
            return cliente;
        }

        public String getServico() {
            return servico;
        }

        public String getProfissional() {
            return profissional;
        }

        public String getSituacao() {
            return situacao;
        }
    }
}
