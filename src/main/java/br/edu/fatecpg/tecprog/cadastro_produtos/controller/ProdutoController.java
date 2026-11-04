package br.edu.fatecpg.tecprog.cadastro_produtos.controller;

import br.edu.fatecpg.tecprog.cadastro_produtos.model.Produto;
import br.edu.fatecpg.tecprog.cadastro_produtos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@Controller
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @ModelAttribute("categorias")
    public List<String> categorias() {
        return List.of("Eletrônico", "Vestuário", "Alimento", "Outros");
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/cadastrar";
    }

    @GetMapping("/cadastrar")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new Produto());
        return "cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrarProduto(@Valid @ModelAttribute Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return "cadastrar";
        }
        service.salvar(produto);
        return "redirect:/lista";
    }

    @GetMapping("/lista")
    public String exibirLista(@RequestParam(required = false) String busca, Model model) {
        model.addAttribute("produtos", service.listar(busca));
        model.addAttribute("busca", busca);
        return "lista";
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        service.remover(id);
        return "redirect:/lista";
    }
}