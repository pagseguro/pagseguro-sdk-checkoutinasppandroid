![ps5.png](https://bitbucket.org/repo/4naLKz/images/1051242651-ps5.png)

# Guia de Integração #
* **
**Biblioteca Android PagSeguro UOL - Checkout in App Manual de Uso**

* **
**Para utilizar o Checkout in App**

Esta versão do Checkout In App foi descontinuada, e por isso, não será evoluída. Contudo, manteremos ela disponível no seu estado atual. Caso esta versão atenda o seu negócio e você queira fazer uso da mesma, entre em contato conosco através do link abaixo:
[http://app.pipefy.com/public/form/503TdrFl](http://app.pipefy.com/public/form/503TdrFl)
 
* **
**Histórico de Versões**    

- 0.0.9 : **Correção de bug e atualização das dependências** - 09/01/2019

          Correção de bug: API do Wallet apontado para o ambiente de QA em PRD

          Atualizado as dependências para as versões mais recente

          Removido as dependências desnecessárias
- 0.0.6 : **Correção do item de segurança** - 23/10/2018
- 0.0.5 : **Inclusão de mensagens de erro** - 05/01/2018

          Inclusão de mensagens de erro especificas para cada erro

          Adição do campo creditCardName no app modelo para pagamento com Checkout Transparent
- 0.0.4 : **Adição do campo extraAmount no pagamento com o Checkout Transparent** - 19/12/2017

          Adição do campo extraAmount no pagamento com o Checkout Transparent

          Refactory do app modelo
- 0.0.3 : **Correção de bugs** - 05/10/2017

          Erro de certificado SSL no Android 4.4

          Erro ao remover o cartão da carteira
- 0.0.2 : **Melhorias e correção de bugs** - 14/09/2017

          Método de download de boleto

          Método para limpeza de dados do cartão principal

          Ajuste no tipo de dado do valor do pagamento com Carteira PagSeguro
- 0.0.1 : **Atualização do SDK**  - 18/08/2017

* **

**Copyright**

Todos os direitos reservados. O UOL é uma marca comercial do UNIVERSO ONLINE S / A. O logotipo do UOL é uma marca comercial do UNIVERSO ONLINE S / A. Outras marcas, nomes, logotipos e marcas são de propriedade de seus respectivos proprietários.
As informações contidas neste documento pertencem ao UNIVERSO ONLINE S/A. Todos os direitos reservados. UNIVERSO ONLINE S/A. - Av. Faria Lima, 1384, 6º andar, São Paulo / SP, CEP 01452-002, Brasil.
O serviço PagSeguro não é, nem pretende ser comparável a serviços financeiros oferecidos por instituições financeiras ou administradoras de cartões de crédito, consistindo apenas de uma forma de facilitar e monitorar a execução das transações de comércio electrónico através da gestão de pagamentos. Qualquer transação efetuada através do PagSeguro está sujeita e deve estar em conformidade com as leis da República Federativa do Brasil.
Aconselhamos que você leia os termos e condições cuidadosamente.

* **

**Aviso Legal**

O UOL não oferece garantias de qualquer tipo (expressas, implícitas ou estatutárias) com relação às informações nele contidas. O UOL não assume nenhuma responsabilidade por perdas e danos (diretos ou indiretos), causados por erros ou omissões, ou resultantes da utilização deste documento ou a informação contida neste documento ou resultantes da aplicação ou uso do produto ou serviço aqui descrito. O UOL reserva o direito de fazer qualquer tipo de alterações a quaisquer informações aqui contidas sem aviso prévio.

* **

**Visão Geral**

A biblioteca Checkout in App tem como foco auxiliar desenvolvedores que desejam prover em seus aplicativos toda a praticidade e segurança fornecida pelo PagSeguro no segmento de pagamentos móveis através de smartphones e tablets. Para ajudar a entender como a biblioteca pode ser utilizada, apresentamos os seguintes cenários:

•    Cenário Exemplo: Solução de pagamentos com Checkout in App Carteira. A empresa X desenvolve um aplicativo para seus clientes permitindo-os efetuar pagamento de serviços prestados ou itens (produtos) vendidos. Neste cenário o aplicativo da empresa X faz uso da biblioteca PagSeguro "Checkout in App" autorizando a Library com a sua conta PagSeguro (E-mail vendedor e Token referente da conta). Os clientes da empresa X que utilizam o aplicativo para realizar o pagamento em um ambiente seguro para autenticação do usuário utilizando uma conta PagSeguro (usuário comprador). Após autenticação o usuário do aplicativo da empresa X poderá realizar pagamentos utilizando sua conta PagSeguro(usuário comprador). A empresa X receberá os pagamentos em sua conta PagSeguro configurada como vendedor na Lib Checkout in App.

•    Cenário Exemplo: Solução de pagamentos com Checkout in App. A empresa X desenvolve um aplicativo para seus clientes permitindo-os efetuar pagamento de serviços prestados ou itens (produtos) vendidos. Neste cenário o aplicativo da empresa X faz uso da biblioteca PagSeguro "Checkout in App" autorizando a Library com a sua conta PagSeguro (E-mail vendedor e Token referente da conta). Os clientes da empresa X realizam o pagamento no app sem sair do App, todos os dados do cartão são passados no próprio aplicativo da empresa. A empresa X receberá os pagamentos em sua conta PagSeguro configurada como vendedor na Lib Checkout in App.

* **

**Conceitos Básicos**

Antes de fazer uso da biblioteca é importante que o desenvolvedor realize alguns procedimentos básicos, além de assimilar alguns conceitos importantes para o correto funcionamento de sua aplicação. É necessário ter em mãos o token da conta PagSeguro que será configurado como vendedor (Seller), tal token pode ser obtido no ibanking do PagSeguro. (Vide tópico abaixo).

* **

**Instalação**

[Saiba como integrar seu aplicativo Android utilizando o Checkout in App](https://devs.pagseguro.uol.com.br/docs/checkout-in-app-android)


* **

**UOL - O melhor conteúdo**

© 1996-2017 O melhor conteúdo. Todos os direitos reservados.
UNIVERSO ONLINE S/A - CNPJ/MF 01.109.184/0001-95 - Av. Brigadeiro Faria Lima, 1.384, São Paulo - SP - CEP 01452-002 
<hr>

