import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Usuario {
    private int id;
    private String nome;
    private boolean premium; //Premium não paga multa
    private int livrosEmprestados;

    public Usuario(int id, String nome, boolean premium) {
        this.id = id;
        this.nome = nome;
        this.premium = premium;
        this.livrosEmprestados = 0;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public boolean isPremium() { return premium; }
    public int getLivrosEmprestados() { return livrosEmprestados; }

    public void adicionarEmprestimo() {
        this.livrosEmprestados++;
    }

    public void removerEmprestimo() {
        this.livrosEmprestados = 0;
    }

    @Override
    public String toString() {
        return "Usuário [" + id + "] " + nome + " | Premium: " + premium + " | Empréstimos Ativos: " + livrosEmprestados;
    }
}

class Livro {
    private int id;
    private String titulo;
    private boolean disponivel;

    public Livro(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.disponivel = true;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public boolean isDisponivel() { return disponivel; }

    public void emprestar() {

    }

    public void devolver() {
        this.disponivel = true;
    }

    @Override
    public String toString() {
        return "Livro [" + id + "] " + titulo + " | Disponível: " + (disponivel ? "Sim" : "Não");
    }
}

public class Main {
    private static List<Usuario> usuarios = new ArrayList<>();
    private static List<Livro> livros = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    private static final int LIMITE_EMPRESTIMOS = 3;

    public static void main(String[] args) {
        System.out.println("=== Sistema de Gestão de Biblioteca ===");
        int opcao = 0;

        while (opcao != 7) {
            System.out.println("\nMenu:");
            System.out.println("1. Cadastrar Livro");
            System.out.println("2. Cadastrar Usuário");
            System.out.println("3. Buscar Livro por Título");
            System.out.println("4. Emprestar Livro");
            System.out.println("5. Devolver Livro");
            System.out.println("6. Calcular Multa de Atraso");
            System.out.println("7. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); //Limpar buffer

            switch (opcao) {
                case 1: cadastrarLivro(); break;
                case 2: cadastrarUsuario(); break;
                case 3: buscarLivro(); break;
                case 4: emprestarLivro(); break;
                case 5: devolverLivro(); break;
                case 6: calcularMulta(); break;
                case 7: System.out.println("Encerrando..."); break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarLivro() {
        System.out.print("ID do Livro: "); int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Título: "); String titulo = scanner.nextLine();
        livros.add(new Livro(id, titulo));
        System.out.println("Livro cadastrado com sucesso!");
    }

    private static void cadastrarUsuario() {
        System.out.print("ID do Usuário: "); int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nome: "); String nome = scanner.nextLine();
        System.out.print("É usuário Premium? (true/false): "); boolean premium = scanner.nextBoolean();
        usuarios.add(new Usuario(id, nome, premium));
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void buscarLivro() {
        System.out.print("Digite o título exato do livro: ");
        String tituloBuscado = scanner.nextLine();

        for (Livro l : livros) {
            if (l.getTitulo() == tituloBuscado) {
                System.out.println("Livro encontrado: " + l);
                return;
            }
        }
        System.out.println("Livro não encontrado no acervo.");
    }

    private static void emprestarLivro() {
        System.out.print("ID do Usuário: "); int idUsu = scanner.nextInt();
        System.out.print("ID do Livro: "); int idLiv = scanner.nextInt();

        Usuario usuario = buscarUsuarioPorId(idUsu);
        Livro livro = buscarLivroPorId(idLiv);

        if (usuario == null || livro == null) {
            System.out.println("Usuário ou livro inválido.");
            return;
        }

        if (!livro.isDisponivel()) {
            System.out.println("Livro já está emprestado!");
            return;
        }

        if (usuario.getLivrosEmprestados() <= LIMITE_EMPRESTIMOS) {
            livro.emprestar();
            usuario.adicionarEmprestimo();
            System.out.println("Empréstimo realizado com sucesso!");
        } else {
            System.out.println("Limite de empréstimos atingido!");
        }
    }

    private static void devolverLivro() {
        System.out.print("ID do Usuário: "); int idUsu = scanner.nextInt();
        System.out.print("ID do Livro: "); int idLiv = scanner.nextInt();

        Usuario usuario = buscarUsuarioPorId(idUsu);
        Livro livro = buscarLivroPorId(idLiv);

        if (usuario != null && livro != null) {
            livro.devolver();
            usuario.removerEmprestimo();
            System.out.println("Devolução realizada com sucesso!");
        } else {
            System.out.println("Dados inválidos.");
        }
    }

    private static void calcularMulta() {
        System.out.print("ID do Usuário: "); int idUsu = scanner.nextInt();
        System.out.print("Dias de atraso: "); int dias = scanner.nextInt();

        Usuario usuario = buscarUsuarioPorId(idUsu);
        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
            return;
        }

        double valorMulta;
        if (usuario.isPremium()) {
            valorMulta = dias * 2.50;
        } else {
            valorMulta = 0.0;
        }

        System.out.println("Valor da multa a pagar: R$ " + valorMulta);
    }

    private static Usuario buscarUsuarioPorId(int id) {
        for (Usuario u : usuarios) {
            if (u.getId() == id) return u;
        }
        return null;
    }

    private static Livro buscarLivroPorId(int id) {
        for (Livro l : livros) {
            if (l.getId() == id) return l;
        }
        return null;
    }
}