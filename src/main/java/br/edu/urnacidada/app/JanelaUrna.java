package br.edu.urnacidada.app;

import br.edu.urnacidada.excecao.VotoInvalidoException;
import br.edu.urnacidada.modelo.Candidato;
import br.edu.urnacidada.servico.UrnaService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.List;

public class JanelaUrna extends JFrame {
    private final UrnaService urna;
    private final JLabel cargo = new JLabel();
    private final JLabel numeroDigitado = new JLabel(" ");
    private final JTextArea ficha = new JTextArea();
    private final JLabel avisoNulo = new JLabel("VOTO NULO", SwingConstants.CENTER);
    private final JLabel fotoSelecionada = new JLabel();
    private final JPanel listaCandidatos = new JPanel();
    private boolean aguardandoConfirmacaoNulo;

    public JanelaUrna(UrnaService urna) {
        this.urna = urna;
        setTitle("Urna Cidadã — Simulação educativa");
        setSize(1180, 720);
        setMinimumSize(new Dimension(980, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        montarTela();
        atualizarTela();
    }

    private void montarTela() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(238, 240, 242));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBorder(new EmptyBorder(12, 18, 0, 18));
        topo.setOpaque(false);
        JLabel titulo = new JLabel("URNA ELETRÔNICA — SIMULAÇÃO EDUCATIVA");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        cargo.setFont(new Font("Arial", Font.BOLD, 20));
        cargo.setHorizontalAlignment(SwingConstants.RIGHT);
        topo.add(titulo, BorderLayout.WEST);
        JPanel topoDireita = new JPanel(new BorderLayout(8, 0));
        topoDireita.setOpaque(false);
        JButton apuracao = new JButton("APURAÇÃO");
        apuracao.setFont(new Font("Arial", Font.BOLD, 11));
        apuracao.addActionListener(e -> abrirApuracao());
        topoDireita.add(apuracao, BorderLayout.WEST);
        topoDireita.add(cargo, BorderLayout.EAST);
        topo.add(topoDireita, BorderLayout.EAST);
        add(topo, BorderLayout.NORTH);

        listaCandidatos.setLayout(new BoxLayout(listaCandidatos, BoxLayout.Y_AXIS));
        listaCandidatos.setBorder(new EmptyBorder(10, 10, 10, 10));
        listaCandidatos.setBackground(Color.WHITE);
        JScrollPane candidatosScroll = new JScrollPane(listaCandidatos);
        candidatosScroll.setPreferredSize(new Dimension(410, 0));
        candidatosScroll.setBorder(BorderFactory.createTitledBorder("Candidatos disponíveis"));
        add(candidatosScroll, BorderLayout.WEST);

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBorder(new EmptyBorder(10, 10, 10, 10));
        centro.setOpaque(false);
        JPanel tela = new JPanel(new BorderLayout(8, 8));
        tela.setBackground(Color.WHITE);
        tela.setBorder(BorderFactory.createTitledBorder("Digite o número do candidato"));
        numeroDigitado.setFont(new Font("Monospaced", Font.BOLD, 38));
        numeroDigitado.setHorizontalAlignment(SwingConstants.CENTER);
        tela.add(numeroDigitado, BorderLayout.NORTH);
        ficha.setEditable(false);
        ficha.setLineWrap(true);
        ficha.setWrapStyleWord(true);
        ficha.setFont(new Font("Arial", Font.PLAIN, 18));
        ficha.setBackground(new Color(250, 250, 250));
        ficha.setBorder(new EmptyBorder(20, 20, 20, 20));
        tela.add(criarAreaFicha(), BorderLayout.CENTER);
        tela.add(criarRodapeTela(), BorderLayout.SOUTH);
        centro.add(tela, BorderLayout.CENTER);
        centro.add(criarTeclado(), BorderLayout.SOUTH);
        add(centro, BorderLayout.CENTER);

        JPanel acoes = new JPanel(new GridLayout(3, 1, 8, 8));
        acoes.setBorder(new EmptyBorder(10, 0, 10, 18));
        acoes.setPreferredSize(new Dimension(150, 0));
        JButton branco = botao("BRANCO", Color.WHITE);
        branco.setForeground(Color.BLACK);
        JButton corrigir = botao("CORRIGE", new Color(238, 145, 45));
        JButton confirmar = botao("CONFIRMA", new Color(45, 155, 90));
        acoes.add(branco); acoes.add(corrigir); acoes.add(confirmar);
        corrigir.addActionListener(e -> limparNumero());
        branco.addActionListener(e -> branco());
        confirmar.addActionListener(e -> confirmar());
        add(acoes, BorderLayout.EAST);
    }

    private JPanel criarTeclado() {
        JPanel teclado = new JPanel(new GridLayout(4, 3, 6, 6));
        teclado.setBorder(new EmptyBorder(5, 70, 5, 70));
        teclado.setOpaque(false);
        for (int i = 1; i <= 9; i++) adicionarTecla(teclado, String.valueOf(i));
        teclado.add(new JLabel());
        adicionarTecla(teclado, "0");
        teclado.add(new JLabel());
        return teclado;
    }

    private void adicionarTecla(JPanel teclado, String numero) {
        JButton tecla = new JButton(numero);
        tecla.setFont(new Font("Arial", Font.BOLD, 22));
        tecla.setFocusPainted(false);
        tecla.addActionListener(e -> adicionarNumero(numero));
        teclado.add(tecla);
    }

    private JButton botao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 13));
        botao.setForeground(Color.WHITE);
        botao.setBackground(cor);
        botao.setFocusPainted(false);
        return botao;
    }

    private void adicionarNumero(String numero) {
        if (aguardandoConfirmacaoNulo) return;
        String atual = numeroDigitado.getText().trim();
        if (atual.length() < 5) {
            numeroDigitado.setText(atual + numero);
            consultar();
        }
    }

    private void limparNumero() {
        numeroDigitado.setText(" ");
        ficha.setText("");
        avisoNulo.setVisible(false);
        fotoSelecionada.setIcon(null);
        aguardandoConfirmacaoNulo = false;
    }

    private JPanel criarAreaFicha() {
        JPanel area = new JPanel(new BorderLayout(20, 0));
        area.setBackground(new Color(250, 250, 250));
        fotoSelecionada.setPreferredSize(new Dimension(180, 180));
        fotoSelecionada.setHorizontalAlignment(SwingConstants.CENTER);
        fotoSelecionada.setVerticalAlignment(SwingConstants.CENTER);
        fotoSelecionada.setBorder(null);
        JPanel painelFoto = new JPanel(new BorderLayout());
        painelFoto.setBackground(Color.WHITE);
        painelFoto.setBorder(BorderFactory.createTitledBorder("Foto"));
        painelFoto.setPreferredSize(new Dimension(190, 205));
        painelFoto.add(fotoSelecionada, BorderLayout.CENTER);
        avisoNulo.setFont(new Font("Arial", Font.BOLD, 30));
        avisoNulo.setForeground(Color.BLACK);
        avisoNulo.setBorder(new EmptyBorder(15, 10, 35, 10));
        avisoNulo.setVisible(false);
        JPanel informacoes = new JPanel(new BorderLayout());
        informacoes.setBackground(new Color(250, 250, 250));
        informacoes.add(ficha, BorderLayout.CENTER);
        informacoes.add(avisoNulo, BorderLayout.SOUTH);
        area.add(informacoes, BorderLayout.CENTER);
        area.add(painelFoto, BorderLayout.EAST);
        return area;
    }

    private JPanel criarRodapeTela() {
        JPanel rodape = new JPanel();
        rodape.setLayout(new BoxLayout(rodape, BoxLayout.Y_AXIS));
        rodape.setBackground(Color.WHITE);
        JLabel instrucoes = new JLabel("Aperte a tecla:   CONFIRMA para CONFIRMAR este voto     CORRIGE para REINICIAR este voto");
        instrucoes.setFont(new Font("Arial", Font.PLAIN, 13));
        instrucoes.setBorder(new EmptyBorder(6, 8, 4, 8));
        rodape.add(instrucoes);
        return rodape;
    }

    private void consultar() {
        String texto = numeroDigitado.getText().trim();
        if (texto.isEmpty()) {
            ficha.setText("");
            fotoSelecionada.setIcon(null);
            return;
        }
        try {
            Candidato c = urna.consultarCandidato(Integer.parseInt(texto));
            if (c == null) {
                Candidato parcial = urna.consultarPorPrefixo(texto);
                if (parcial != null && texto.length() < String.valueOf(parcial.getNumero()).length()) {
                    ficha.setText("PARTIDO: " + parcial.getSiglaPartido() + "\n" +
                            parcial.getNomePartido() + "\n\nDigite os próximos números.");
                    avisoNulo.setVisible(false);
                } else {
                    if (texto.length() >= 2) {
                        mostrarVotoNulo();
                    } else {
                        ficha.setText("Digite os próximos números.");
                        avisoNulo.setVisible(false);
                    }
                }
                fotoSelecionada.setIcon(null);
            } else {
                ficha.setText(fichaCandidato(c));
                fotoSelecionada.setIcon(carregarImagem(c.getImagemPath(), 180));
            }
        } catch (NumberFormatException ex) {
            ficha.setText("Número inválido.");
            fotoSelecionada.setIcon(null);
        }
    }

    private String fichaCandidato(Candidato c) {
        return "NOME: " + c.getNome() + "\n\n" +
                "PARTIDO: " + c.getSiglaPartido() + "\n" +
                c.getNomePartido() + "\n\n" +
                "CARGO: " + c.getCargo();
    }

    private void confirmar() {
        if (aguardandoConfirmacaoNulo) {
            confirmarVotoNulo();
            return;
        }
        String texto = numeroDigitado.getText().trim();
        if (texto.isEmpty()) {
            mensagem("Digite um número.");
            return;
        }
        try {
            int numero = Integer.parseInt(texto);
            Candidato candidato = urna.consultarCandidato(numero);
            if (candidato == null) {
                confirmarNulo();
                return;
            }
            urna.votar(numero);
            avancar();
        } catch (NumberFormatException ex) {
            mensagem("Digite um número válido.");
        } catch (VotoInvalidoException ex) {
            mensagem(ex.getMessage());
        }
    }

    private void confirmarNulo() {
        mostrarVotoNulo();
    }

    private void mostrarVotoNulo() {
        aguardandoConfirmacaoNulo = true;
        ficha.setText("NÚMERO ERRADO");
        fotoSelecionada.setIcon(null);
        avisoNulo.setVisible(true);
    }

    private void confirmarVotoNulo() {
        try {
            urna.votarNulo();
            avancar();
        } catch (VotoInvalidoException ex) {
            mensagem(ex.getMessage());
        }
    }

    private void branco() {
        try { urna.votarBranco(); avancar(); }
        catch (VotoInvalidoException ex) { mensagem(ex.getMessage()); }
    }

    private void avancar() {
        SomUrna.tocarConfirmacao();
        limparNumero();
        if (urna.finalizada()) {
            urna.reiniciar();
            atualizarTela();
        } else atualizarTela();
    }

    private void abrirApuracao() {
        JDialog dialogo = new JDialog(this, "Apuração dos votos", true);
        dialogo.setSize(560, 430);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(8, 8));

        JPanel topoApuracao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topoApuracao.add(new JLabel("Data (AAAA-MM-DD):"));
        JTextField data = new JTextField(LocalDate.now().toString(), 12);
        JButton consultar = new JButton("Consultar");
        topoApuracao.add(data);
        topoApuracao.add(consultar);
        dialogo.add(topoApuracao, BorderLayout.NORTH);

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultado.setBorder(new EmptyBorder(12, 12, 12, 12));
        dialogo.add(new JScrollPane(resultado), BorderLayout.CENTER);
        consultar.addActionListener(e -> consultarApuracao(data.getText().trim(), resultado));
        dialogo.setVisible(true);
    }

    private void consultarApuracao(String textoData, JTextArea resultado) {
        try {
            LocalDate data = LocalDate.parse(textoData);
            List<br.edu.urnacidada.modelo.Voto> votos = urna.getRepositorio().listarPorData(data);
            long vereadores = votos.stream().filter(v -> v.getCargo().equals("Vereador") && v.getNumero() > 0).count();
            long prefeitos = votos.stream().filter(v -> v.getCargo().equals("Prefeito") && v.getNumero() > 0).count();
            long brancos = votos.stream().filter(v -> v.getNumero() == 0).count();
            long nulos = votos.stream().filter(v -> v.getNumero() < 0).count();
            StringBuilder sb = new StringBuilder("APURAÇÃO DO DIA ").append(data).append("\n\n")
                    .append("Total de registros: ").append(votos.size()).append("\n")
                    .append("Votos brancos: ").append(brancos).append("\n")
                    .append("Votos nulos: ").append(nulos).append("\n\n")
                    .append("--- PREFEITO ---\n");
            adicionarRanking(sb, "Prefeito", votos);
            sb.append("\n--- VEREADOR ---\n");
            adicionarRanking(sb, "Vereador", votos);
            sb.append("\n* Resultado educativo. Não representa uma apuração oficial.");
            resultado.setText(sb.toString());
        } catch (DateTimeParseException ex) {
            resultado.setText("Data inválida. Use o formato AAAA-MM-DD.");
        } catch (Exception ex) {
            resultado.setText("Não foi possível consultar a apuração:\n" + ex.getMessage());
        }
    }

    private void adicionarRanking(StringBuilder sb, String cargo, List<br.edu.urnacidada.modelo.Voto> todosVotos) {
        Map<Integer, Long> contagem = new LinkedHashMap<>();
        todosVotos.stream()
                .filter(v -> v.getCargo().equals(cargo) && v.getNumero() > 0)
                .forEach(v -> contagem.put(v.getNumero(), contagem.getOrDefault(v.getNumero(), 0L) + 1));
        
        List<Map.Entry<Integer, Long>> ranking = new java.util.ArrayList<>(contagem.entrySet());
        ranking.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        if (ranking.isEmpty()) {
            sb.append("Nenhum voto registrado para este cargo.\n");
            return;
        }

        for (int i = 0; i < ranking.size(); i++) {
            var entry = ranking.get(i);
            Candidato c = urna.getPrefeitos().stream().filter(can -> can.getNumero() == entry.getKey()).findFirst().orElse(null);
            if (c == null) c = urna.getVereadores().stream().filter(can -> can.getNumero() == entry.getKey()).findFirst().orElse(null);
            
            String nome = (c != null) ? c.getNome() + " (" + c.getSiglaPartido() + ")" : "Candidato " + entry.getKey();
            sb.append(String.format("%dº %-25s %d voto(s)%n", i + 1, nome, entry.getValue()));
            if (i == 0) {
                String destaque = cargo.equals("Prefeito") ? "Vencedor da simulação: " : "Mais votado da simulação: ";
                sb.append("   > ").append(destaque).append(nome).append("\n");
            }
        }
    }

    private void atualizarTela() {
        cargo.setText("VOTE PARA " + urna.getCargoAtual().toUpperCase());
        atualizarLista();
    }

    private void atualizarLista() {
        listaCandidatos.removeAll();
        adicionarTituloLista("PREFEITOS");
        for (Candidato candidato : urna.getPrefeitos()) adicionarCard(candidato);
        adicionarTituloLista("VEREADORES");
        for (Candidato candidato : urna.getVereadores()) adicionarCard(candidato);
        listaCandidatos.revalidate();
        listaCandidatos.repaint();
    }

    private void adicionarTituloLista(String titulo) {
        JLabel label = new JLabel(titulo);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setBorder(new EmptyBorder(8, 4, 5, 4));
        listaCandidatos.add(label);
    }

    private void adicionarCard(Candidato c) {
        JPanel card = new JPanel(new BorderLayout(8, 4));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));
        card.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(210, 210, 210)));
        card.setBackground(Color.WHITE);
        JLabel foto = new JLabel(carregarImagem(c.getImagemPath()));
        foto.setPreferredSize(new Dimension(68, 68));
        card.add(foto, BorderLayout.WEST);
        JLabel texto = new JLabel("<html><b>" + c.getNumero() + " — " + c.getNome() + "</b><br>" +
                c.getSiglaPartido() + " — " + c.getNomePartido() + "</html>");
        card.add(texto, BorderLayout.CENTER);
        listaCandidatos.add(card);
    }

    private ImageIcon carregarImagem(String caminho) {
        return carregarImagem(caminho, 68);
    }

    private ImageIcon carregarImagem(String caminho, int tamanho) {
        java.net.URL recurso = getClass().getClassLoader().getResource(caminho);
        Image imagem = null;
        if (recurso != null) imagem = new ImageIcon(recurso).getImage();
        else if (new File("src/main/resources/" + caminho).exists())
            imagem = new ImageIcon("src/main/resources/" + caminho).getImage();
        if (imagem == null) {
            BufferedImage vazio = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = vazio.createGraphics();
            g.setColor(new Color(225, 228, 230)); g.fillRect(0, 0, tamanho, tamanho);
            g.setColor(Color.DARK_GRAY); g.drawString("foto", tamanho / 2 - 12, tamanho / 2); g.dispose();
            imagem = vazio;
        }
        return new ImageIcon(imagem.getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH));
    }

    private void mensagem(String texto) { JOptionPane.showMessageDialog(this, texto); }
}
