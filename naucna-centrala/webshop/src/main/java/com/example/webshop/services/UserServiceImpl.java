package com.example.webshop.services;

import com.example.webshop.dto.CasopisDTO;
import com.example.webshop.dto.CorrectionDTO;
import com.example.webshop.dto.UredniciRecenzentiDTO;
import com.example.webshop.model.*;
import com.example.webshop.repository.CasopisRepository;
import com.example.webshop.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements  UserService {

    @Autowired
    CasopisRepository casopisRepository;

    @Autowired
    KorisnikRepository korisnikRepository;

    @Override
    public UredniciRecenzentiDTO getUrednikRecenzent(Long casopisId, String username){
        UredniciRecenzentiDTO ret = new UredniciRecenzentiDTO();
        ret.setRecenzenti(new ArrayList<>());
        ret.setUrednici(new ArrayList<>());

        HashMap<String,Korisnik> korisnici = new HashMap<>();

        Casopis casopis = casopisRepository.getOne(casopisId);
        for (NaucnaOblast naucnaOblast: casopis.getNaucneOblasti()) {
            for (Korisnik k : naucnaOblast.getKorisnici()) {
                try {
                    korisnici.put(k.getUsername(),k);
                    System.out.println(k.getId());
                }catch (Exception e){
                    continue;
                }
            }
        }

        for (Korisnik k:korisnici.values()) {
            Authority authority=((List<Authority>)k.getAuthorities()).get(0);
            System.out.println(authority.getName());
            if(authority.getName().equals("ROLE_UREDNIK") && !k.getUsername().equals(username)){
                ret.getUrednici().add(k.getUsername());
            }

            if(authority.getName().equals("ROLE_RECENZENT") && !k.getUsername().equals(username)){
                System.out.println("OVO JE RECENZENT");
                ret.getRecenzenti().add(k.getUsername());
            }
        }

        return ret;
    }

    @Override
    public CorrectionDTO getCorrectionData(Long casopisId){
        CorrectionDTO ret = new CorrectionDTO();
        Casopis casopis = casopisRepository.getOne(casopisId);

        ret.setId(casopisId);
        ret.setNaziv(casopis.getNaziv());
        ret.setClanarina(casopis.getClanarina());
        ret.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());
        for (NaucnaOblast no:casopis.getNaucneOblasti()) {
            ret.getNaucneOblasti().add(no.getId());
        }
        for (NacinPlacanja np:casopis.getNaciniPlacanja()) {
            ret.getNaciniPlacanja().add(np.getId());
        }

        return ret;
    }

    @Override
    public List<CasopisDTO> getMyPapers(String username){
        Korisnik korisnik = korisnikRepository.findOneByUsername(username);

        List<CasopisDTO> ret = new ArrayList<>();

        for (Casopis casopis:korisnik.getCasopisiGlavni()) {
            CasopisDTO dto = new CasopisDTO();
            dto.setId(casopis.getId());
            dto.setNaziv(casopis.getNaziv());
            dto.setClanarina(casopis.getClanarina());
            dto.setIssn(casopis.getIssn());
            dto.setAktiviran(casopis.getAktiviran());
            dto.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());

            ret.add(dto);
        }

        return ret;
    }

    public List<CasopisDTO> getAllPapers(){
        List<CasopisDTO> ret = new ArrayList<>();
        List<Casopis> casopisList = casopisRepository.findAllActive();

        for (Casopis casopis:casopisList) {
            CasopisDTO dto = new CasopisDTO();
            dto.setId(casopis.getId());
            dto.setNaziv(casopis.getNaziv());
            dto.setClanarina(casopis.getClanarina());
            dto.setIssn(casopis.getIssn());
            dto.setAktiviran(casopis.getAktiviran());
            dto.setKomeSeNaplacuje(casopis.getKomeSeNaplacuje());

            ret.add(dto);
        }

        return ret;
    }
}
