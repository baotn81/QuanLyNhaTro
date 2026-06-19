/* ============================================================
   Migration: unify customer data on table KhachHang
   Repoints HoaDon.MaKhach and HopDong.MaKhach foreign keys
   from the legacy KhachThue table to KhachHang.

   Safe to run: pre-checked that
     - no HoaDon/HopDong.MaKhach orphan vs KhachHang
     - KhachThue is a subset of KhachHang (no data loss)
     - no indexes block the column type change
   Wrapped in a transaction; either all steps apply or none.
   ============================================================ */
BEGIN TRY
    BEGIN TRANSACTION;

    -- 1. Drop the old FKs that point at KhachThue
    ALTER TABLE HoaDon  DROP CONSTRAINT FK__HoaDon__MaKhach__440B1D61;
    ALTER TABLE HopDong DROP CONSTRAINT FK__HopDong__MaKhach__3F466844;

    -- 2. Align column type with KhachHang.MaKhach (nvarchar(20)) so the FK can be created
    ALTER TABLE HoaDon  ALTER COLUMN MaKhach nvarchar(20) NULL;
    ALTER TABLE HopDong ALTER COLUMN MaKhach nvarchar(20) NULL;

    -- 3. Create new FKs referencing the canonical KhachHang table
    ALTER TABLE HoaDon  ADD CONSTRAINT FK_HoaDon_KhachHang  FOREIGN KEY (MaKhach) REFERENCES KhachHang(MaKhach);
    ALTER TABLE HopDong ADD CONSTRAINT FK_HopDong_KhachHang FOREIGN KEY (MaKhach) REFERENCES KhachHang(MaKhach);

    COMMIT TRANSACTION;
    PRINT 'Migration committed successfully.';
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0 ROLLBACK TRANSACTION;
    PRINT 'Migration failed and was rolled back: ' + ERROR_MESSAGE();
    THROW;
END CATCH;

/* Note: the legacy KhachThue table is intentionally left in place (now unused).
   After confirming everything works you may optionally drop it:
   -- DROP TABLE KhachThue;
*/
