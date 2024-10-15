package Mts.Crud.Services;

import java.math.BigDecimal;
import java.util.List;

import Mts.Crud.Dto.MvtStkDto;

public interface MvtStkService {

  
    List<MvtStkDto> mvtStkArticle(Integer idArticle);
  
    MvtStkDto entreeStock(MvtStkDto dto);
  
    MvtStkDto sortieStock(MvtStkDto dto);
  
    MvtStkDto correctionStockPos(MvtStkDto dto);
  
    MvtStkDto correctionStockNeg(MvtStkDto dto);
  
  
  }
