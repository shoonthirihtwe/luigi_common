package jp.co.ichain.luigi2.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * DepositDetailVo
 *
 * @author : [VJP] n.huy.hoang
 * @createdAt : 2021-06-16
 * @updatedAt : 2021-06-16
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class DepositDetailVo {

	Integer id;

	Integer tenantId;

	Date entryDate;

	Integer batchNo;

	Integer cashDetailNo;

	String contractNo;

	String applicationNo;

	Date dueDate;

	Integer totalPremiumAmount;

	Integer depositAmount;

	Integer commissionWithheld;

	Integer compensationTax;

	Date clearingDate;

	Date suspenceDate;

	Date deleteDate;

	Date cashMatchingDate;

	String cashDetailDtatus;

	String paymentResultCode;

	String comment;

	Date premiumDueDate;

	Integer premiumSequenceNo;

	@JsonIgnore
	Integer updateCount;

	@JsonIgnore
	Date createdAt;

	@JsonIgnore
	String createdBy;

	@JsonIgnore
	Date updatedAt;

	@JsonIgnore
	String updatedBy;

	@JsonIgnore
	Date deletedAt;

	@JsonIgnore
	String deletedBy;

}
