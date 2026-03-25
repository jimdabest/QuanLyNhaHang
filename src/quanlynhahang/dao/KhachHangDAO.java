package quanlynhahang.dao;

import quanlynhahang.dto.KhachHangDTO;

import java.util.ArrayList;

public class KhachHangDAO implements IDAO<KhachHangDTO, String>{
    public KhachHangDAO() {
        super();
    }

    @Override
    public boolean insert(KhachHangDTO obj) {
        return false;
    }

    @Override
    public boolean update(KhachHangDTO obj) {
        return false;
    }

    @Override
    public boolean delete(String key) {
        return false;
    }

    @Override
    public ArrayList<KhachHangDTO> getAll() {
        return null;
    }

    @Override
    public KhachHangDTO getById(String key) {
        return null;
    }
}
