# Store Manager

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/AndCandido/store-manager/blob/main/LICENSE)

# Descrição do projeto

O projeto tem como finalidade gerenciar produtos, clientes e vendas de uma loja. Possibilitando verificar o estoque dos produtos, parcelas a serem pagas por um cliente, vendas realizadas entre outras funcionalidades.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Data Jpa / Hibernate
- Maven

# Executar aplicação

## Requisitos

- JDK 17

## Execute
```Bash
# Para clonar a aplicação em seu dispositivo
git clone https://github.com/AndCandido/store-manager.git

# Para iniciar a aplicação
./mvnw spring-boot:run
````
Assim o servidor estará disponível na porta ```8080```

# Recursos da aplicação

## Produtos

### Objeto

```

Product {
  id:            num
  name:          string          Required
  productsSold:  ProductSold[]
  price:         num             Required
  stockQuantity: num             Required
  createdAt:     Date  
} 

```

### Rotas

```id: num```

```GET /products``` -> Listar todos os produtos<br/>
```GET /products/{id}``` -> Retorna o produto específico pelo ID<br/>
```POST /products``` -> Salva um produto<br/>
```PUT /products/{id}``` -> Atualiza um produto específico pelo ID<br/>
```DELETE /products/{id}``` -> Deleta um produto <br/>

## Clientes

### Objeto

```

Customer {
  id:            UUID
  name:         string        Required
  nickname:     string
  cpf:          string        Required
  address:      string        Required
  phone:        string
  sales:        Sale[]
  installment:  Installment[]
  createdAt:    Date  
} 

```

### Rotas

```id: UUID```

```GET /customers``` -> Listar todos os clientes<br/>
```GET /customers/{id}``` -> Retorna o cliente específico pelo ID<br/>
```POST /customers``` -> Salva um cliente<br/>
```PUT /customers/{id}``` -> Atualiza um cliente específico pelo ID<br/>
```DELETE /customers/{id}``` -> Deleta um cliente <br/>

## Vendas

### Objeto

```

Sale {
  id:            UUID
  customer:      Customer
  productsSold:  ProductSold[]   Required
  installments:  Installment[]   Required
  price:         num Required
  createdAt:     Date  
} 

```

### Rotas

```id: UUID```

```GET /sales``` -> Listar todas as vendas<br/>
```GET /sales/{id}``` -> Retorna a venda específico pelo ID<br/>
```POST /sales``` -> Salva uma venda<br/>
```PUT /sales/{id}``` -> Atualiza uma venda específico pelo ID<br/>
```DELETE /sales/{id}``` -> Deleta uma venda <br/>

## Parcelas

### Objeto

```
Installment {
  id:             UUID
  dueDate:        Date           Required
  price:          num            Required
  PaymentMethod:  PaymentMethod
  isPaid          boolean        Required
  customer:       Customer
  sale:           Sale
  createdAt:      Date  
} 

```

### Rotas

```id: UUID```

```PATCH /installments/{id}``` -> Atualiza um campo específica de uma parcela pelo ID<br/>
Nota: Rota pensada para atualizar o campo ```isPaid``` de uma parcela, quando uma parcela for paga.

## Produto Vendido

### Objeto
```
ProductSold {
  id:          UUID
  productId:   num    Required
  quantity:    num    Required
}
```

## Método de Pagamento (ENUM)
 
```
PaymentMethod {
  "money"
  "debit card"
  "credit card"
  "pix"
  "negotiated"
  "none"
}
```
