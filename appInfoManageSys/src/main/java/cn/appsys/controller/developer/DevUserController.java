package cn.appsys.controller.developer;

import cn.appsys.pojo.*;
import cn.appsys.service.developer.*;
import cn.appsys.tools.Constants;
import cn.appsys.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.core.util.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value="/dev/flatform/devUser")
public class DevUserController {
	private Logger logger = Logger.getLogger(DevUserController.class);
	@Resource
	private DevUserService devUserService;
	@Resource 
	private DataDictionaryService dataDictionaryService;
	@Resource 
	private AppCategoryService appCategoryService;
	@Resource
	private AppVersionService appVersionService;
	
	@RequestMapping(value="/list")
	public String getAppInfoList(Model model,HttpSession session,
							@RequestParam(value="querySoftwareName",required=false) String querySoftwareName,
							@RequestParam(value="pageIndex",required=false) String pageIndex){
		
		logger.info("getAppInfoList -- > querySoftwareName: " + querySoftwareName);
		logger.info("getAppInfoList -- > pageIndex: " + pageIndex);
		
		Integer devId = ((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId();
		List<DevUser> devUserInfoList = null;
		List<DataDictionary> statusList = null;
		List<DataDictionary> flatFormList = null;
		//页面容量
		int pageSize = Constants.pageSize;
		//当前页码
		Integer currentPageNo = 1;
		
		if(pageIndex != null){
			try{
				currentPageNo = Integer.valueOf(pageIndex);
			}catch (NumberFormatException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		//总数量（表）
		int totalCount = 0;
		try {
			totalCount = devUserService.getDevUserCount(querySoftwareName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//总页数
		PageSupport pages = new PageSupport();
		pages.setCurrentPageNo(currentPageNo);
		pages.setPageSize(pageSize);
		pages.setTotalCount(totalCount);
		int totalPageCount = pages.getTotalPageCount();
		//控制首页和尾页
		if(currentPageNo < 1){
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount){
			currentPageNo = totalPageCount;
		}
		try {
			devUserInfoList = devUserService.getDevUserList(querySoftwareName, currentPageNo-1, pageSize);
			statusList = this.getDataDictionaryList("APP_STATUS");
			flatFormList = this.getDataDictionaryList("APP_FLATFORM");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("statusList", statusList);
		model.addAttribute("flatFormList", flatFormList);
		model.addAttribute("devUserInfoList", devUserInfoList);
		model.addAttribute("pages", pages);
		model.addAttribute("querySoftwareName", querySoftwareName);
		return "developer/userinfolist";
	}
	
	public List<DataDictionary> getDataDictionaryList(String typeCode){
		List<DataDictionary> dataDictionaryList = null;
		try {
			dataDictionaryList = dataDictionaryService.getDataDictionaryList(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataDictionaryList;
	}

	@RequestMapping(value="/datadictionarylist.json",method=RequestMethod.GET)
	@ResponseBody
	public List<DataDictionary> getDataDicList (@RequestParam String tcode){
		logger.debug("getDataDicList tcode ============ " + tcode);
		return this.getDataDictionaryList(tcode);
	}

	@RequestMapping(value="/getBackendlist.json",method=RequestMethod.GET)
	@ResponseBody
	public Object getAppCategoryList (){
		logger.debug("getAppCategoryList pid ============ ");
		List<BackendUser> list = getCategoryList();
		return JSONArray.toJSON(list);
	}

	public List<BackendUser> getCategoryList (){
		List<BackendUser> backendUserList = null;
		try {
		    backendUserList = devUserService.getBackEnd();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return backendUserList;
	}
//	/**
//	 * 增加app信息（跳转到新增appinfo页面）
//	 * @param appInfo
//	 * @return
//	 */
//	@RequestMapping(value="/appinfoadd",method=RequestMethod.GET)
//	public String add(@ModelAttribute("appInfo") AppInfo appInfo){
//		return "developer/appinfoadd";
//	}
//
//	/**
//	 * 保存新增appInfo（主表）的数据
//	 * @param appInfo
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping(value="/appinfoaddsave",method=RequestMethod.POST)
//	public String addSave(AppInfo appInfo,HttpSession session,HttpServletRequest request,
//					@RequestParam(value="a_logoPicPath",required= false) MultipartFile attach){
//
//		String logoPicPath =  null;
//		String logoLocPath =  null;
//		if(!attach.isEmpty()){
//			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
//			logger.info("uploadFile path: " + path);
//			String oldFileName = attach.getOriginalFilename();//原文件名
//			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
//			int filesize = 500000;
//			if(attach.getSize() > filesize){//上传大小不得超过 50k
//				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_4);
//				return "developer/appinfoadd";
//            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
//			   ||prefix.equalsIgnoreCase("jepg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式
//				 String fileName = appInfo.getAPKName() + ".jpg";//上传LOGO图片命名:apk名称.apk
//				 File targetFile = new File(path,fileName);
//				 if(!targetFile.exists()){
//					 targetFile.mkdirs();
//				 }
//				 try {
//					attach.transferTo(targetFile);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_2);
//					return "developer/appinfoadd";
//				}
//				 logoPicPath = request.getContextPath()+"/statics/uploadfiles/"+fileName;
//				 logoLocPath = path+File.separator+fileName;
//			}else{
//				request.setAttribute("fileUploadError", Constants.FILEUPLOAD_ERROR_3);
//				return "developer/appinfoadd";
//			}
//		}
//		appInfo.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
//		appInfo.setCreationDate(new Date());
//		appInfo.setLogoPicPath(logoPicPath);
//		appInfo.setLogoLocPath(logoLocPath);
//		appInfo.setDevId(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
//		appInfo.setStatus(1);
//		try {
//			if(appInfoService.add(appInfo)){
//				return "redirect:/dev/flatform/app/list";
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "developer/appinfoadd";
//	}
//
//	/**
//	 * 增加appversion信息（跳转到新增app版本页面）
//	 * @param appInfo
//	 * @return
//	 */
//	@RequestMapping(value="/appversionadd",method=RequestMethod.GET)
//	public String addVersion(@RequestParam(value="id")String appId,
//							 @RequestParam(value="error",required= false)String fileUploadError,
//							 AppVersion appVersion,Model model){
//		logger.debug("fileUploadError============> " + fileUploadError);
//		if(null != fileUploadError && fileUploadError.equals("error1")){
//			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
//		}else if(null != fileUploadError && fileUploadError.equals("error2")){
//			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
//		}else if(null != fileUploadError && fileUploadError.equals("error3")){
//			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
//		}
//		appVersion.setAppId(Integer.parseInt(appId));
//		List<AppVersion> appVersionList = null;
//		try {
//			appVersionList = appVersionService.getAppVersionList(Integer.parseInt(appId));
//			appVersion.setAppName((appInfoService.getAppInfo(Integer.parseInt(appId),null)).getSoftwareName());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		model.addAttribute("appVersionList", appVersionList);
//		model.addAttribute(appVersion);
//		model.addAttribute("fileUploadError",fileUploadError);
//		return "developer/appversionadd";
//	}
//	/**
//	 * 保存新增appversion数据（子表）-上传该版本的apk包
//	 * @param appInfo
//	 * @param appVersion
//	 * @param session
//	 * @param request
//	 * @param attach
//	 * @return
//	 */
//	@RequestMapping(value="/addversionsave",method=RequestMethod.POST)
//	public String addVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
//						@RequestParam(value="a_downloadLink",required= false) MultipartFile attach ){
//		String downloadLink =  null;
//		String apkLocPath = null;
//		String apkFileName = null;
//		if(!attach.isEmpty()){
//			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
//			logger.info("uploadFile path: " + path);
//			String oldFileName = attach.getOriginalFilename();//原文件名
//			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
//			if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
//				 String apkName = null;
//				 try {
//					apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
//				 } catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				 }
//				 if(apkName == null || "".equals(apkName)){
//					 return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
//							 +"&error=error1";
//				 }
//				 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
//				 File targetFile = new File(path,apkFileName);
//				 if(!targetFile.exists()){
//					 targetFile.mkdirs();
//				 }
//				 try {
//					attach.transferTo(targetFile);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
//							 +"&error=error2";
//				}
//				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
//				apkLocPath = path+File.separator+apkFileName;
//			}else{
//				return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId()
//						 +"&error=error3";
//			}
//		}
//		appVersion.setCreatedBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
//		appVersion.setCreationDate(new Date());
//		appVersion.setDownloadLink(downloadLink);
//		appVersion.setApkLocPath(apkLocPath);
//		appVersion.setApkFileName(apkFileName);
//		try {
//			if(appVersionService.appsysadd(appVersion)){
//				return "redirect:/dev/flatform/app/list";
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "redirect:/dev/flatform/app/appversionadd?id="+appVersion.getAppId();
//	}
//
//	@RequestMapping(value="/{appid}/sale",method=RequestMethod.PUT)
//	@ResponseBody
//	public Object sale(@PathVariable String appid,HttpSession session){
//		HashMap<String, Object> resultMap = new HashMap<String, Object>();
//		Integer appIdInteger = 0;
//		try{
//			appIdInteger = Integer.parseInt(appid);
//		}catch(Exception e){
//			appIdInteger = 0;
//		}
//		resultMap.put("errorCode", "0");
//		resultMap.put("appId", appid);
//		if(appIdInteger>0){
//			try {
//				DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
//				AppInfo appInfo = new AppInfo();
//				appInfo.setId(appIdInteger);
//				appInfo.setModifyBy(devUser.getId());
//				if(appInfoService.appsysUpdateSaleStatusByAppId(appInfo)){
//					resultMap.put("resultMsg", "success");
//				}else{
//					resultMap.put("resultMsg", "success");
//				}
//			} catch (Exception e) {
//				resultMap.put("errorCode", "exception000001");
//			}
//		}else{
//			//errorCode:0为正常
//			resultMap.put("errorCode", "param000001");
//		}
//
//		/*
//		 * resultMsg:success/failed
//		 * errorCode:exception000001
//		 * appId:appId
//		 * errorCode:param000001
//		 */
//		return resultMap;
//	}
//
//	/**
//	 * 判断APKName是否唯一
//	 * @param apkName
//	 * @return
//	 */
//	@RequestMapping(value="/apkexist.json",method=RequestMethod.GET)
//	@ResponseBody
//	public Object apkNameIsExit(@RequestParam String APKName){
//		HashMap<String, String> resultMap = new HashMap<String, String>();
//		if(StringUtils.isNullOrEmpty(APKName)){
//			resultMap.put("APKName", "empty");
//		}else{
//			AppInfo appInfo = null;
//			try {
//				appInfo = appInfoService.getAppInfo(null, APKName);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			if(null != appInfo)
//				resultMap.put("APKName", "exist");
//			else
//				resultMap.put("APKName", "noexist");
//		}
//		return JSONArray.toJSONString(resultMap);
//	}

	@RequestMapping(value="/devUserview/{id}",method=RequestMethod.GET)
	public String view(@PathVariable String id,Model model){
		DevUser devUser = null;
		try {
			devUser = devUserService.getLoginUserForView(Integer.parseInt(id));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("devUser",devUser);
		return "developer/devUserinfoview";
	}

//
//	/**
//	 * 修改app信息，包括：修改app基本信息（appInfo）和修改版本信息（appVersion）
//	 * 分为两步实现：
//	 * 1 修改app基本信息（appInfo）
//	 * 2 修改版本信息（appVersion）
//	 */
//
//	/**
//	 * 修改appInfo信息（跳转到修改appInfo页面）
//	 * @param id
//	 * @param model
//	 * @return
//	 */
//	@RequestMapping(value="/appinfomodify",method=RequestMethod.GET)
//	public String modifyAppInfo(@RequestParam("id") String id,
//								@RequestParam(value="error",required= false)String fileUploadError,
//								Model model){
//		AppInfo appInfo = null;
//		logger.debug("modifyAppInfo --------- id: " + id);
//		if(null != fileUploadError && fileUploadError.equals("error1")){
//			fileUploadError = Constants.FILEUPLOAD_ERROR_1;
//		}else if(null != fileUploadError && fileUploadError.equals("error2")){
//			fileUploadError	= Constants.FILEUPLOAD_ERROR_2;
//		}else if(null != fileUploadError && fileUploadError.equals("error3")){
//			fileUploadError = Constants.FILEUPLOAD_ERROR_3;
//		}else if(null != fileUploadError && fileUploadError.equals("error4")){
//			fileUploadError = Constants.FILEUPLOAD_ERROR_4;
//		}
//		try {
//			appInfo = appInfoService.getAppInfo(Integer.parseInt(id),null);
//		}catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		model.addAttribute(appInfo);
//		model.addAttribute("fileUploadError",fileUploadError);
//		return "developer/appinfomodify";
//	}
//
	/**
	 * 修改最新的appVersion信息（跳转到修改appVersion页面）
	 * @param versionId
	 * @param appId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/devUsermodify",method=RequestMethod.GET)
	public String modifyAppVersion(@RequestParam("id") String versionId,
									Model model){
		DevUser devUser = null;

		try {
			devUser = devUserService.getLoginUserForView(Integer.parseInt(versionId));
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("devUser",devUser);
		return "developer/devUsermodify";
	}
//
//	/**
//	 * 保存修改后的appVersion
//	 * @param appVersion
//	 * @param session
//	 * @return
//	 */
//	@RequestMapping(value="/appversionmodifysave",method=RequestMethod.POST)
//	public String modifyAppVersionSave(AppVersion appVersion,HttpSession session,HttpServletRequest request,
//					@RequestParam(value="attach",required= false) MultipartFile attach){
//
//		String downloadLink =  null;
//		String apkLocPath = null;
//		String apkFileName = null;
//		if(!attach.isEmpty()){
//			String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
//			logger.info("uploadFile path: " + path);
//			String oldFileName = attach.getOriginalFilename();//原文件名
//			String prefix = FilenameUtils.getExtension(oldFileName);//原文件后缀
//			if(prefix.equalsIgnoreCase("apk")){//apk文件命名：apk名称+版本号+.apk
//				 String apkName = null;
//				 try {
//					apkName = appInfoService.getAppInfo(appVersion.getAppId(),null).getAPKName();
//				 } catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				 }
//				 if(apkName == null || "".equals(apkName)){
//					 return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
//							 +"&aid="+appVersion.getAppId()
//							 +"&error=error1";
//				 }
//				 apkFileName = apkName + "-" +appVersion.getVersionNo() + ".apk";
//				 File targetFile = new File(path,apkFileName);
//				 if(!targetFile.exists()){
//					 targetFile.mkdirs();
//				 }
//				 try {
//					attach.transferTo(targetFile);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
//							 +"&aid="+appVersion.getAppId()
//							 +"&error=error2";
//				}
//				downloadLink = request.getContextPath()+"/statics/uploadfiles/"+apkFileName;
//				apkLocPath = path+File.separator+apkFileName;
//			}else{
//				return "redirect:/dev/flatform/app/appversionmodify?vid="+appVersion.getId()
//						 +"&aid="+appVersion.getAppId()
//						 +"&error=error3";
//			}
//		}
//		appVersion.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
//		appVersion.setModifyDate(new Date());
//		appVersion.setDownloadLink(downloadLink);
//		appVersion.setApkLocPath(apkLocPath);
//		appVersion.setApkFileName(apkFileName);
//		try {
//			if(appVersionService.modify(appVersion)){
//				return "redirect:/dev/flatform/app/list";
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return "developer/appversionmodify";
//	}
//
//	/**
//	 * 修改操作时，删除文件（logo图片/apk文件），并更新数据库（app_info/app_version）
//	 * @param fileUrlPath
//	 * @param fileLocPath
//	 * @param flag
//	 * @param id
//	 * @return
//	 */
//	@RequestMapping(value = "/delfile",method=RequestMethod.GET)
//	@ResponseBody
//	public Object delFile(@RequestParam(value="flag",required=false) String flag,
//						 @RequestParam(value="id",required=false) String id){
//		HashMap<String, String> resultMap = new HashMap<String, String>();
//		String fileLocPath = null;
//		if(flag == null || flag.equals("") ||
//			id == null || id.equals("")){
//			resultMap.put("result", "failed");
//		}else if(flag.equals("logo")){//删除logo图片（操作app_info）
//			try {
//				fileLocPath = (appInfoService.getAppInfo(Integer.parseInt(id), null)).getLogoLocPath();
//				File file = new File(fileLocPath);
//			    if(file.exists())
//			     if(file.delete()){//删除服务器存储的物理文件
//						if(appInfoService.deleteAppLogo(Integer.parseInt(id))){//更新表
//							resultMap.put("result", "success");
//						 }
//			    }
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}else if(flag.equals("apk")){//删除apk文件（操作app_version）
//			try {
//				fileLocPath = (appVersionService.getAppVersionById(Integer.parseInt(id))).getApkLocPath();
//				File file = new File(fileLocPath);
//			    if(file.exists())
//			     if(file.delete()){//删除服务器存储的物理文件
//						if(appVersionService.deleteApkFile(Integer.parseInt(id))){//更新表
//							resultMap.put("result", "success");
//						 }
//			    }
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return JSONArray.toJSONString(resultMap);
//	}
//
	/**
	 * 保存修改后的appInfo
	 * @param appInfo
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/Backendmodifysave",method=RequestMethod.POST)
	public String modifySave(DevUser devUser,HttpSession session,HttpServletRequest request){
		devUser.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        devUser.setModifyDate(new Date());
		try {
			if(devUserService.modifyBackendUser(devUser)){
				return "redirect:/dev/flatform/devUser/list";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/devUsermodify";
	}
    @RequestMapping(value="/BackendmodifysaveByPerson",method=RequestMethod.POST)
    public String BackendmodifysaveByPerson(DevUser devUser,HttpSession session,HttpServletRequest request){
        devUser.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
        devUser.setModifyDate(new Date());
        try {
            if(devUserService.modifyBackendUser(devUser)){
                session.setAttribute(Constants.DEV_USER_SESSION, devUser);
                return "redirect:/dev/flatform/devUser/list";
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "developer/devUsermodify";
    }

    @RequestMapping(value="/DevUserUpdate",method=RequestMethod.GET)
    public String DevUserUpdate(){
        return "developer/devUserUpdate";
    }
    @RequestMapping(value="/DevUserAdd",method=RequestMethod.GET)
    public String DevUserAdd(){
        return "developer/devUserAdd";
    }
	@RequestMapping(value="/DevUserAddSave",method=RequestMethod.POST)
	public String DevUserAddSave(DevUser devUser,HttpSession session,HttpServletRequest request){
		devUser.setCreationDate(new Date());
		devUser.setModifyBy(((DevUser)session.getAttribute(Constants.DEV_USER_SESSION)).getId());
		devUser.setModifyDate(new Date());
		try {
			if(devUserService.addDevUser(devUser)){
				return "redirect:/dev/flatform/devUser/list";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "developer/devUserAdd";
	}
//
//
	@RequestMapping(value="/delDevUser.json")
	@ResponseBody
	public Object delDevUser(@RequestParam String id){
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(id)){
			resultMap.put("delResult", "notexist");
		}else{
			try {
				if(devUserService.delDevUser(Integer.parseInt(id)))
					resultMap.put("delResult", "true");
				else
					resultMap.put("delResult", "false");
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONArray.toJSONString(resultMap);
	}
}
