package br.com.mi81.api_produtos.controller;

import br.com.mi81.api_produtos.entity.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/v1/api_produtos")
public class ProdutoController {

    private final List<Produto> produtos = new ArrayList<>();
    private final AtomicLong sequencialId = new AtomicLong(1);

    public ProdutoController() {
        produtos.add(new Produto(
                sequencialId.getAndIncrement(),
                "Mouse sem fio G pro x",
                new BigDecimal("600.00")
        ));

        produtos.add(new Produto(
                sequencialId.getAndIncrement(),
                "xing ling",
                new BigDecimal("40.00")
        ));
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrar(@RequestBody Produto produto){
        produto.setId(sequencialId.getAndIncrement());
        produtos.add(produto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(produto);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listar(){
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id){
        return produtos.stream()
                .filter(produto -> produto.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado){
        for(Produto produto : produtos) {
            if (produto.getId().equals(id)) {
                produto.setNome(produtoAtualizado.getNome());
                produto.setPreco(produtoAtualizado.getPreco());
                return ResponseEntity.ok(produto);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Produto> atualizarParcialmente(@PathVariable Long id, @RequestBody Produto produtoAtualizado){
        for(Produto produto : produtos){
            if(produto.getId().equals(id)){
                if(produtoAtualizado.getNome() != null){
                    produto.setNome(produtoAtualizado.getNome());
                }
                if(produtoAtualizado.getPreco() != null){
                    produto.setPreco(produtoAtualizado.getPreco());
                }
            return ResponseEntity.ok(produto);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        boolean removido = produtos.removeIf(produto -> produto.getId().equals(id));
        if(!removido){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}