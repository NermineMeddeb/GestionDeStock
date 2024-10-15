package Mts.Crud.Services.implementation;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Mts.Crud.Dto.MvtStkDto;
import Mts.Crud.Exceptions.ErrorCodes;
import Mts.Crud.Exceptions.InvalidEntity;
import Mts.Crud.Model.TypeMvtStk;
import Mts.Crud.Repository.MvtStockRepository;
import Mts.Crud.Services.ArticleService;
import Mts.Crud.Services.MvtStkService;
import Mts.Crud.Validateur.MvtStockValidateur;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {

  private MvtStockRepository repository;
  private ArticleService articleService;

  @Autowired
  public MvtStkServiceImpl(MvtStockRepository repository, ArticleService articleService) {
    this.repository = repository;
    this.articleService = articleService;
  }

  

  @Override
  public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
    return repository.findAllByArticleId(idArticle).stream()
        .map(MvtStkDto::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public MvtStkDto entreeStock(MvtStkDto dto) {
    return entreePositive(dto, TypeMvtStk.ENTREE);
  }

  @Override
  public MvtStkDto sortieStock(MvtStkDto dto) {
    return sortieNegative(dto, TypeMvtStk.SORTIE);
  }

  @Override
  public MvtStkDto correctionStockPos(MvtStkDto dto) {
    return entreePositive(dto, TypeMvtStk.CORRECTION_POS);
  }

  @Override
  public MvtStkDto correctionStockNeg(MvtStkDto dto) {
    return sortieNegative(dto, TypeMvtStk.CORRECTION_NEG);
  }

  private MvtStkDto entreePositive(MvtStkDto dto, TypeMvtStk typeMvtStk) {
    List<String> errors = MvtStockValidateur.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Article is not valid {}", dto);
      throw new InvalidEntity("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
    }
    dto.setQuantite(
        BigDecimal.valueOf(
            Math.abs(dto.getQuantite().doubleValue())
        )
    );
    dto.setTypeMvt(typeMvtStk);
    return MvtStkDto.fromEntity(
        repository.save(MvtStkDto.toEntity(dto))
    );
  }

  private MvtStkDto sortieNegative(MvtStkDto dto, TypeMvtStk typeMvtStk) {
    List<String> errors = MvtStockValidateur.validate(dto);
    if (!errors.isEmpty()) {
      log.error("Article is not valid {}", dto);
      throw new InvalidEntity("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
    }
    dto.setQuantite(
        BigDecimal.valueOf(
            Math.abs(dto.getQuantite().doubleValue()) * -1
        )
    );
    dto.setTypeMvt(typeMvtStk);
    return MvtStkDto.fromEntity(
        repository.save(MvtStkDto.toEntity(dto))
    );
  }}