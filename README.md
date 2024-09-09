#  Java MySQL - Sistema OS-TecMaqli
È um sistema desktop Windows para gestão de ordem de serviços de uma assistência técnica de lavanderia e cozinhas industriais.

## Instruções para instalação e uso do aplicativo
### Pré requisitos
1) Ter o Java **versão 8** instalado. 

[download Java 8](https://www.java.com/pt-BR/download/)

2) Ter um banco de dados local baseado no **MySQL 8**

[download MySQL 8](https://dev.mysql.com/downloads/installer/)

O que faz essa aplicação?

Pensa em uma oficina mecânica, uma loja de eletrônicos ou qualquer outro lugar que precisa controlar suas ordens de serviço, cadastrar clientes, cadastrar usuarios e emitir relatorios. 
essa aplicação é a sua ferramenta mágica para:

Gerenciar clientes: Adicione,edite,exclua, pesquise e organize seus clientes com facilidade.
Criar ordens de serviço: Registre todos os detalhes de cada serviço, desde o equipamento até o valor final.
Controlar o status: Acompanhe o andamento de cada ordem, do orçamento até a conclusão.
Imprimir relatórios: Gere relatórios personalizados para acompanhar seus negócios.

![Captura de tela 2024-09-09 002345](https://github.com/user-attachments/assets/60c20810-5ea3-4169-8eb5-9ac91d005d4f)
![Captura de tela 2024-09-09 002442](https://github.com/user-attachments/assets/c5963f50-c912-4208-9371-5d927acaa344)
![Captura de tela 2024-09-09 002237](https://github.com/user-attachments/assets/5c0de823-89f7-4d91-bb2a-aa83843c1c6f)

![gif os java](https://github.com/user-attachments/assets/0a23a35c-e462-40b9-a8bf-faed1db0e633)

O projeto é construído com as seguintes tecnologias:

MySQL: O coração dos dados
Por que MySQL? É um banco de dados relacional popular, conhecido por sua performance, confiabilidade e facilidade de uso.
No seu projeto: Armazena todas as informações sobre clientes, ordens de serviço, produtos e outras entidades relacionadas ao seu sistema. A estrutura de tabelas bem definida permite realizar consultas eficientes e manter a integridade dos dados.

NetBeans: Seu ambiente de desenvolvimento integrado (IDE)
Por que NetBeans? É uma IDE poderosa e gratuita, com ferramentas específicas para desenvolvimento Java, incluindo suporte a criação de interfaces gráficas (GUIs) com Swing.
No seu projeto: Facilita a escrita do código, depuração, compilação e execução da aplicação. Além disso, oferece recursos como autocompletar, refatoração de código e integração com sistemas de controle de versão.

Java JDK 22: A linguagem e a máquina virtual
Por que Java JDK 22? O Java é uma linguagem orientada a objetos, robusta e portável. O JDK 22 é a versão mais recente, oferecendo novas funcionalidades e melhorias de desempenho.
No seu projeto: É a base para a construção da aplicação, desde a lógica de negócio até a interface gráfica. O JDK fornece as classes e bibliotecas necessárias para criar programas Java.

JasperSoft Studio: Relatórios profissionais
Por que JasperSoft Studio? É uma ferramenta poderosa para criar relatórios complexos e personalizados. Permite a criação de layouts flexíveis, a integração com diversas fontes de dados e a exportação para diversos formatos.
No seu projeto: Gera relatórios personalizados das ordens de serviço, facilitando a análise de dados e a geração de documentos para os clientes.

Como essas tecnologias trabalham juntas:
Desenvolvimento: Você utiliza o NetBeans para escrever o código Java, criando a interface gráfica com Swing e implementando a lógica de negócio.
Conexão com o banco de dados: Através do JDBC, seu código Java se conecta ao MySQL para realizar consultas, inserir, atualizar e excluir dados.
Geração de relatórios: O JasperSoft Studio é utilizado para criar templates de relatórios. Em tempo de execução, sua aplicação Java passa os dados do banco de dados para o JasperSoft, que gera os relatórios em formato PDF ou outros formatos.
