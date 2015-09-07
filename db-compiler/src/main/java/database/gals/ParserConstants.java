package database.gals;

public interface ParserConstants
{
    int START_SYMBOL = 37;

    int FIRST_NON_TERMINAL    = 37;
    int FIRST_SEMANTIC_ACTION = 73;

    int[][] PARSER_TABLE =
    {
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  0, -1,  0, -1, -1, -1, -1,  0, -1, -1,  0, -1, -1,  0, -1, -1, -1, -1,  0, -1 },
        {  2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  1, -1,  1, -1, -1, -1, -1,  1, -1, -1,  1, -1, -1,  1, -1, -1, -1, -1,  1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  3, -1,  3, -1, -1, -1, -1,  3, -1, -1,  3, -1, -1,  3, -1, -1, -1, -1,  3, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,  4, -1,  9, -1, -1, -1, -1,  7, -1, -1,  5, -1, -1,  6, -1, -1, -1, -1,  8, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 11, -1, 12, -1, -1, -1, -1, 13, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 14, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 15, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, 17, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 18, 19, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 24, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, 26, 25, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, 27, 27, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 27 },
        { -1, -1, 28, 29, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 30 },
        { -1, -1, -1, -1, -1, -1, 32, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, -1, -1, -1 },
        { -1, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1, -1 },
        { -1, 41, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, 36, -1, 35, 36, 36, 36, 36, 36, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, -1, -1 },
        { -1, 37, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 42, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, 44, -1, -1, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, -1, -1, -1, -1 },
        { -1, 45, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, 47, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 46, -1, -1, -1, -1 },
        { -1, 48, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, 49, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 50, 50, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 52, 53, 54, 55, 56, 57, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 58, 59, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 60, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 61, -1, -1, -1, -1, 62, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 63, -1 },
        { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 64, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 }
    };

    int[][] PRODUCTIONS = 
    {
        {  39,  38, 143 },
        {  37 },
        {   0 },
        {  40,   5, 144 },
        {  41 },
        {  49 },
        {  54 },
        {  69 },
        {  71 },
        {  72 },
        {  17,  42 },
        {  18,   2,  93 },
        {  20,   2,  94,   6,  43,   7 },
        {  25,   2,  95,  26,   2,  96,   6,  43,   7,  97 },
        {  44,  45 },
        {   2,  98,  46,  99 },
        {   8,  43 },
        {   0 },
        {  21,  73 },
        {  22,  75,   6,   3,  76,   7 },
        {  23,  74,   6,   3,  76,   7 },
        {  27,  28,   2, 103,  50,  29,   6, 106,  51, 107,   7 },
        {   6, 104,  47, 105,   7 },
        {   0 },
        {   2, 108,  48 },
        {   8,  47 },
        {   0 },
        {  52,  53 },
        {   3,  83 },
        {   4,  84 },
        {  36,  85 },
        {   8,  51 },
        {   0 },
        {  30,  55, 116,  31, 117,  60, 118,  63 },
        {  57,  56 },
        {  10,  59 },
        {   0 },
        {   2, 114 },
        {   9, 115 },
        {   8,  55 },
        {   0 },
        {   2, 113,  58 },
        {  62,  61 },
        {   8,  60 },
        {   0 },
        {   2, 119 },
        {  32,  64, 120 },
        {   0 },
        {  65, 121,  66 },
        {  57,  67, 122,  52 },
        {  68, 123,  64 },
        {   0 },
        {  11 },
        {  12 },
        {  13 },
        {  14 },
        {  15 },
        {  16 },
        {  33 },
        {  34 },
        {  24,  70 },
        {  20,   2, 133 },
        {  25,   2, 134 },
        {  35,   2, 135 },
        {  19,  18,   2, 136 }
    };

    String[] PARSER_ERROR =
    {
        "",
        "Era esperado fim de programa",
        "Era esperado id",
        "Era esperado numero",
        "Era esperado literal",
        "Era esperado \";\"",
        "Era esperado \"(\"",
        "Era esperado \")\"",
        "Era esperado \",\"",
        "Era esperado \"*\"",
        "Era esperado \".\"",
        "Era esperado \"=\"",
        "Era esperado \">\"",
        "Era esperado \"<\"",
        "Era esperado \">=\"",
        "Era esperado \"<=\"",
        "Era esperado \"<>\"",
        "Era esperado CREATE",
        "Era esperado DATABASE",
        "Era esperado SET",
        "Era esperado TABLE",
        "Era esperado INTEGER",
        "Era esperado VARCHAR",
        "Era esperado CHAR",
        "Era esperado DROP",
        "Era esperado INDEX",
        "Era esperado ON",
        "Era esperado INSERT",
        "Era esperado INTO",
        "Era esperado VALUES",
        "Era esperado SELECT",
        "Era esperado FROM",
        "Era esperado WHERE",
        "Era esperado AND",
        "Era esperado OR",
        "Era esperado DESCRIBE",
        "Era esperado NULL",
        "<BLOCO> inv�lido",
        "<BLOCO_COMPLETO> inv�lido",
        "<SENTENCA> inv�lido",
        "<ACAO> inv�lido",
        "<CRIAR> inv�lido",
        "<CRIAR2> inv�lido",
        "<ATRIBUTOS> inv�lido",
        "<ATRIBUTO> inv�lido",
        "<RATRIBUTOS> inv�lido",
        "<TIPO> inv�lido",
        "<LISTAIDS> inv�lido",
        "<RLISTAIDS> inv�lido",
        "<INCLUIR> inv�lido",
        "<COLUNAS> inv�lido",
        "<LISTAVALORES> inv�lido",
        "<VALOR> inv�lido",
        "<RLISTAVALORES> inv�lido",
        "<SELECIONAR> inv�lido",
        "<CAMPOS> inv�lido",
        "<RCAMPOS> inv�lido",
        "<CAMPO> inv�lido",
        "<CAMPO2> inv�lido",
        "<CAMPO3> inv�lido",
        "<TABELAS> inv�lido",
        "<RTABELAS> inv�lido",
        "<TABELA> inv�lido",
        "<CLAUSULA_WHERE> inv�lido",
        "<CONDICOES> inv�lido",
        "<CONDICAO> inv�lido",
        "<RCONDICAO> inv�lido",
        "<OPERADOR_REL> inv�lido",
        "<OPERADOR_LOG> inv�lido",
        "<EXCLUIR> inv�lido",
        "<EXCLUIR2> inv�lido",
        "<DESCREVER> inv�lido",
        "<SETAR_BANCO> inv�lido"
    };
}
