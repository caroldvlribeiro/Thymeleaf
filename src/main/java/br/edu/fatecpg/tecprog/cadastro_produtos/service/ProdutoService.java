package br.edu.fatecpg.tecprog.cadastro_produtos.service;

import br.edu.fatecpg.tecprog.cadastro_produtos.model.Produto;
import br.edu.fatecpg.tecprog.cadastro_produtos.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> listar(String busca) {
        if (busca == null || busca.isBlank()) {
            return repository.findAll();
        }
        return repository.findByNomeContainingIgnoreCaseOrCategoriaContainingIgnoreCase(busca, busca);
    }

    public void salvar(Produto produto) {
        repository.save(produto);
    }

    public void remover(Long id) {
        repository.deleteById(id);
    }
}