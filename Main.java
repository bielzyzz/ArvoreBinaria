//Trabalho sobre arvore binaria
//alunos: gabriel tonon barreiros - rafael ribeiro moura
import java.util.Scanner;

public class Main {

    private static class ARVORE {
        public int num;
        public ARVORE dir, esq;
        public int altura;
    }

    public static int altura(ARVORE aux) {
        return aux == null ? 0 : aux.altura;
    }

    public static int fatorBalanceamento(ARVORE aux) {
        return aux == null ? 0 : altura(aux.esq) - altura(aux.dir);
    }

    public static ARVORE rotacionarDireita(ARVORE y) {
        ARVORE x = y.esq;
        ARVORE T2 = x.dir;

        x.dir = y;
        y.esq = T2;

        y.altura = Math.max(altura(y.esq), altura(y.dir)) + 1;
        x.altura = Math.max(altura(x.esq), altura(x.dir)) + 1;

        return x;
    }

    public static ARVORE rotacionarEsquerda(ARVORE x) {
        ARVORE y = x.dir;
        ARVORE T2 = y.esq;

        y.esq = x;
        x.dir = T2;

        x.altura = Math.max(altura(x.esq), altura(x.dir)) + 1;
        y.altura = Math.max(altura(y.esq), altura(y.dir)) + 1;

        return y;
    }

    public static ARVORE inserir(ARVORE aux, int num) {
        if (aux == null) {
            aux = new ARVORE();
            aux.num = num;
            aux.esq = null;
            aux.dir = null;
            aux.altura = 1;
            return aux;
        }

        if (num < aux.num) {
            aux.esq = inserir(aux.esq, num);
        } else if (num > aux.num) {
            aux.dir = inserir(aux.dir, num);
        } else {

            return aux;
        }

        aux.altura = 1 + Math.max(altura(aux.esq), altura(aux.dir));

        int balance = fatorBalanceamento(aux);

        if (balance > 1 && num < aux.esq.num) {
            return rotacionarDireita(aux);
        }

        if (balance < -1 && num > aux.dir.num) {
            return rotacionarEsquerda(aux);
        }

        if (balance > 1 && num > aux.esq.num) {
            aux.esq = rotacionarEsquerda(aux.esq);
            return rotacionarDireita(aux);
        }

        if (balance < -1 && num < aux.dir.num) {
            aux.dir = rotacionarDireita(aux.dir);
            return rotacionarEsquerda(aux);
        }

        return aux;
    }

    public static void imprimir(ARVORE aux, int nivel) {
        if (aux != null) {
            imprimir(aux.dir, nivel + 1);
            System.out.println(" ".repeat(nivel * 4) + aux.num);
            imprimir(aux.esq, nivel + 1);
        }
    }

    public static boolean localizar(ARVORE aux, int num) {
        if (aux == null) return false;

        if (aux.num == num) {
            return true;
        } else if (num < aux.num) {
            return localizar(aux.esq, num);
        } else {
            return localizar(aux.dir, num);
        }
    }

    public static ARVORE excluir(ARVORE aux, int num) {
        if (aux == null) return null;

        if (num < aux.num) {
            aux.esq = excluir(aux.esq, num);
        } else if (num > aux.num) {
            aux.dir = excluir(aux.dir, num);
        } else {

            if (aux.esq == null && aux.dir == null) {
                return null;
            }

            if (aux.esq == null) {
                return aux.dir;
            } else if (aux.dir == null) {
                return aux.esq;
            }

            ARVORE sucessor = aux.dir;
            while (sucessor.esq != null) {
                sucessor = sucessor.esq;
            }
            aux.num = sucessor.num;
            aux.dir = excluir(aux.dir, sucessor.num);
        }

        aux.altura = Math.max(altura(aux.esq), altura(aux.dir)) + 1;

        int balance = fatorBalanceamento(aux);

        if (balance > 1 && fatorBalanceamento(aux.esq) >= 0) {
            return rotacionarDireita(aux);
        }

        if (balance < -1 && fatorBalanceamento(aux.dir) <= 0) {
            return rotacionarEsquerda(aux);
        }

        if (balance > 1 && fatorBalanceamento(aux.esq) < 0) {
            aux.esq = rotacionarEsquerda(aux.esq);
            return rotacionarDireita(aux);
        }

        if (balance < -1 && fatorBalanceamento(aux.dir) > 0) {
            aux.dir = rotacionarDireita(aux.dir);
            return rotacionarEsquerda(aux);
        }

        return aux;
    }

    public static ARVORE excluirRaiz(ARVORE raiz) {
        if (raiz == null) return null;
        return excluir(raiz, raiz.num);
    }

    public static ARVORE excluirNoFolha(ARVORE raiz, int num) {
        if (localizar(raiz, num)) {
            ARVORE nodo = excluir(raiz, num);
            if (nodo != null && nodo.esq == null && nodo.dir == null) {
                return excluir(raiz, num);
            }
        }
        return raiz;
    }

    public static ARVORE excluirNoComUmFilho(ARVORE raiz, int num) {
        ARVORE nodo = excluir(raiz, num);
        if (nodo != null && (nodo.esq == null || nodo.dir == null)) {
            return excluir(raiz, num);
        }
        return raiz;
    }

    public static ARVORE excluirNoComDoisFilhos(ARVORE raiz, int num) {
        ARVORE nodo = excluir(raiz, num);
        if (nodo != null && nodo.esq != null && nodo.dir != null) {
            return excluir(raiz, num);
        }
        return raiz;
    }

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        ARVORE a = null;

        System.out.print("Quantos nós você deseja inserir? ");
        int n = entrada.nextInt();

        System.out.println("Insira " + n + " valores (sem repetição!):");

        int count = 0;
        while (count < n) {
            int num = entrada.nextInt();
            if (!localizar(a, num)) {
                a = inserir(a, num);
                count++;
            } else {
                System.out.println("O valor já existe na árvore!. insira outro.");
            }
        }

        boolean continuar = true;

        while (continuar) {
            System.out.println("\nEscolha uma operação:");
            System.out.println("1 - Imprimir árvore");
            System.out.println("2 - Excluir um nó");
            System.out.println("3 - Excluir a raiz");
            System.out.println("4 - Excluir um nó com um filho");
            System.out.println("5 - Excluir um nó com dois filhos");
            System.out.println("6 - Pesquisar um nó");
            System.out.println("7 - Sair");
            System.out.print("Opção: ");

            int opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    System.out.println("Árvore balanceada:");
                    imprimir(a, 0);
                    break;
                case 2:
                    System.out.print("Digite o número a ser excluído: ");
                    int excluirNum = entrada.nextInt();
                    a = excluir(a, excluirNum);
                    System.out.println("Árvore após exclusão:");
                    imprimir(a, 0);
                    break;
                case 3:
                    System.out.println("Excluindo a raiz...");
                    a = excluirRaiz(a);
                    System.out.println("Árvore após exclusão da raiz:");
                    imprimir(a, 0);
                    break;
                case 4:
                    System.out.print("Digite o número do nó com um filho a ser excluído: ");
                    int excluirUmFilhoNum = entrada.nextInt();
                    a = excluirNoComUmFilho(a, excluirUmFilhoNum);
                    System.out.println("Árvore após exclusão do nó com um filho:");
                    imprimir(a, 0);
                    break;
                case 5:
                    System.out.print("Digite o número do nó com dois filhos a ser excluído: ");
                    int excluirDoisFilhosNum = entrada.nextInt();
                    a = excluirNoComDoisFilhos(a, excluirDoisFilhosNum);
                    System.out.println("Árvore após exclusão do nó com dois filhos:");
                    imprimir(a, 0);
                    break;
                case 6:
                    System.out.print("Digite o número a ser pesquisado: ");
                    int pesquisaNum = entrada.nextInt();
                    if (localizar(a, pesquisaNum)) {
                        System.out.println("Número encontrado.");
                    } else {
                        System.out.println("Número não encontrado.");
                    }
                    break;
                case 7:
                    continuar = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }

        entrada.close();
    }
}