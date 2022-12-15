package com.example.blockchain_gateway.controller;

import com.example.blockchain_gateway.domain.AjaxResult;
import com.example.blockchain_gateway.model.regulator.License;
import com.example.blockchain_gateway.model.supplies.Supplies;
import com.example.blockchain_gateway.service.RegulatorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/blockchain/regulator")
@RestController
public class RegulatorController {

    @Autowired
    private RegulatorService regulatorService;

    @RequestMapping("/getAllLicenses")
    public AjaxResult getAllLicenses() {
        try {
            List<License> licensesList = regulatorService.getAllLicenses();
            return AjaxResult.success("Get all licenses successfully", licensesList);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/getAllSupplies")
    public AjaxResult getAllSupplies() {
        try {
            List<Supplies> allSupplies = regulatorService.getAllSupplies();
            return AjaxResult.success("Get all supplies successfully", allSupplies);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/releaseLicense")
    public AjaxResult releaseLicense(@RequestBody License license) {
        try {
            if (StringUtils.isEmpty(license.getLicenseId())
                    && license.getRegulatorId() != null
                    && license.getManufacturerId() != null
                    && !StringUtils.isEmpty(license.getRegulatorName())
                    && !StringUtils.isEmpty(license.getManufacturerName())
                    && !StringUtils.isEmpty(license.getSuppliesId())
                    && !StringUtils.isEmpty(license.getSuppliesName())
                    && !StringUtils.isEmpty(license.getIssueDate())
                    && !StringUtils.isEmpty(license.getExpireDate())) {
                Supplies supplies = regulatorService.getSuppliesById(license.getSuppliesId());
                if (supplies != null) {
                    License newLicense = regulatorService.releaseLicense(license);
                    if (newLicense != null) {
                        return AjaxResult.success("Release license successfully", newLicense);
                    }
                }
            }
            return AjaxResult.error("Failed to release license, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/removeLicense")
    public AjaxResult removeLicense(@RequestParam("licenseId") String licenseId) {
        try {
            if (!StringUtils.isEmpty(licenseId)
                    && regulatorService.getLicenseById(licenseId) != null) {
                if (regulatorService.checkLicenseNotUsedById(licenseId)) {
                    String deleteId = regulatorService.removeLicense(licenseId);
                    if (deleteId != null) {
                        return AjaxResult.success("Remove license successfully", deleteId);
                    }
                }
            }
            return AjaxResult.error("Failed to cancel license, the license has been used in manufacturing");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/releaseSupplies")
    public AjaxResult releaseSupplies(@RequestBody Supplies supplies) {
        try {
            if (StringUtils.isEmpty(supplies.getId())
                    && !StringUtils.isEmpty(supplies.getName())
                    && !StringUtils.isEmpty(supplies.getIsOtc())
                    && !StringUtils.isEmpty(supplies.getRemarks())
                    && regulatorService.getSuppliesByName(supplies.getName()) == null) {
                Supplies newSupplies = regulatorService.releaseSupplies(supplies);
                if (newSupplies != null) {
                    return AjaxResult.success("Release supplies successfully", newSupplies);
                }
            }
            return AjaxResult.error("Failed to release supplies, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/removeSupplies")
    public AjaxResult removeSupplies(@RequestParam("suppliesId") String suppliesId) {
        try {
            if (regulatorService.getSuppliesById(suppliesId) != null) {
                if (regulatorService.checkSuppliesNotUsedById(suppliesId)) {
                    String deleteId = regulatorService.removeSupplies(suppliesId);
                    if (deleteId != null) {
                        return AjaxResult.success("Remove supplies successfully", deleteId);
                    }
                }
            }
            return AjaxResult.error("Failed to cancel supplies, the supplies are already in orders");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/updateLicense")
    public AjaxResult updateLicense(@RequestBody License license) {
        try {
            if (!StringUtils.isEmpty(license.getLicenseId())
                    && regulatorService.getLicenseById(license.getLicenseId()) != null) {
                License newLicense = regulatorService.updateLicense(license);
                if (newLicense != null) {
                    return AjaxResult.success("Update license successfully", license);
                }
            }
            return AjaxResult.error("Failed to update license, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/updateSupplies")
    public AjaxResult updateSupplies(@RequestBody Supplies supplies) {
        try {
            if (!StringUtils.isEmpty(supplies.getId())
                    && regulatorService.getSuppliesById(supplies.getId()) != null) {
                Supplies newSupplies = regulatorService.updateSupplies(supplies);
                if (newSupplies != null) {
                    return AjaxResult.success("Update supplies successfully", supplies);
                }
            }
            return AjaxResult.error("Failed to update supplies, please check the input");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
