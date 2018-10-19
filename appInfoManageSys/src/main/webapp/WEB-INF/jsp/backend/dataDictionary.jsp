<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="common/header.jsp"%>
<div class="clearfix"></div>
<div class="row">
	<div class="col-md-12">
		<div class="x_panel">
			<div class="x_title">
				<h2>
					数据字典页面 <i class="fa fa-user"></i><small>${userSession.userName}
						- 在此页面您可以通过搜索等其他操作对数据字典的信息进行修改、查看等管理操作。^_^</small>
				</h2>
				<div class="clearfix"></div>
			</div>
			<div class="x_content">
				<form method="post" action="/appManager/flatform/dataDictionary/list">
					<input type="hidden" name="pageIndex" value="1" />
			    <ul>
					<li>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-12">数据类型</label>
							<div class="col-md-6 col-sm-6 col-xs-12">
								<select name="typeName" class="form-control" id="typeName"></select>
							</div>
						</div>
					</li>
					<li><button type="submit" class="btn btn-primary"> 查 &nbsp;&nbsp;&nbsp;&nbsp;询 </button></li>
				</ul>
			</form>
		</div>
	</div>
</div>
<div class="col-md-12 col-sm-12 col-xs-12">
	<div class="x_panel">
		<div class="x_content">
			<p class="text-muted font-13 m-b-30"></p>
			<div id="datatable-responsive_wrapper"
				class="dataTables_wrapper form-inline dt-bootstrap no-footer">
				<div class="row">
					<div class="col-sm-12">
						<table id="datatable-responsive" class="table table-striped table-bordered dt-responsive nowrap dataTable no-footer dtr-inline collapsed"
							cellspacing="0" width="100%" role="grid" aria-describedby="datatable-responsive_info" style="width: 100%;">
							<thead>
								<tr role="row">
									<th class="sorting_asc" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="First name: activate to sort column descending"
										aria-sort="ascending">类型编码</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="Last name: activate to sort column ascending">
										类型名称</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="Last name: activate to sort column ascending">
										类型值ID</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="Last name: activate to sort column ascending">
										类型值Name</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="Last name: activate to sort column ascending">
										创建者</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="Last name: activate to sort column ascending">
										创建时间</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="Last name: activate to sort column ascending">
										更新者</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										aria-label="Last name: activate to sort column ascending">
										最新更新时间</th>
									<th class="sorting" tabindex="0"
										aria-controls="datatable-responsive" rowspan="1" colspan="1"
										style="width: 124px;"
										aria-label="Last name: activate to sort column ascending">
										操作</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="dataList" items="${dataList}" varStatus="status">
									<tr role="row" class="odd">
										<td tabindex="0" class="sorting_1">${dataList.typeCode}</td>
										<td>${dataList.typeName}</td>
										<td>${dataList.valueId }</td>
										<td>${dataList.valueName }</td>
										<td><c:choose>
											<c:when test="${dataList.createdBy eq 1}">
												系统管理员
											</c:when>
											<c:when test="${dataList.createdBy eq 2}">
												超级管理员
											</c:when>
											<c:when test="${dataList.createdBy eq 3}">
												测试管理员
											</c:when>
											<c:otherwise>
												${dataList.createdBy}
											</c:otherwise>
										</c:choose></td>
										<td><fmt:formatDate value="${dataList.creationDate }"></fmt:formatDate></td>
										<td><c:choose>
											<c:when test="${dataList.modifyBy eq 1}">
												系统管理员
											</c:when>
											<c:when test="${dataList.modifyBy eq 2}">
												超级管理员
											</c:when>
											<c:when test="${dataList.modifyBy eq 3}">
												测试管理员
											</c:when>
											<c:otherwise>
												${dataList.modifyBy}
											</c:otherwise>
										</c:choose></td>
										<td><fmt:formatDate value="${dataList.modifyDate }"></fmt:formatDate></td>
										<td>
										<div class="btn-group">
                      <button type="button" class="btn btn-danger">点击操作</button>
                      <button type="button" class="btn btn-danger dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                        <span class="caret"></span>
                        <span class="sr-only">Toggle Dropdown</span>
                      </button>
                      <ul class="dropdown-menu" role="menu">
                        <li><a class="addDataDictionaryForValue" dataid="${dataList.id }" data-toggle="tooltip" data-placement="top" title="" data-original-title="新增数据类型值信息">新增数据类型值</a>
                        </li>
						  <li><a class="addDataDictionary" dataid="${dataList.id }" data-toggle="tooltip" data-placement="top" title="" data-original-title="新增新的类型信息">新增数据类型</a>
						  </li>
                        <li><a class="modifyDataDictionary"
							   dataid="${dataList.id}"
											data-toggle="tooltip" data-placement="top" title="" data-original-title="修改数据信息">修改数据</a>
                        </li>
                        <li><a  class="viewDataDictionary" dataid="${dataList.id }"  data-toggle="tooltip" data-placement="top" title="" data-original-title="查看数据信息">查看</a></li>
                      </ul>
                    </div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-5">
						<div class="dataTables_info" id="datatable-responsive_info"
							role="status" aria-live="polite">共${pages.totalCount }条记录
							${pages.currentPageNo }/${pages.totalPageCount }页</div>
					</div>
					<div class="col-sm-7">
						<div class="dataTables_paginate paging_simple_numbers"
							id="datatable-responsive_paginate">
							<ul class="pagination">
								<c:if test="${pages.currentPageNo > 1}">
									<li class="paginate_button previous"><a
										href="javascript:page_nav(document.forms[0],1);"
										aria-controls="datatable-responsive" data-dt-idx="0"
										tabindex="0">首页</a>
									</li>
									<li class="paginate_button "><a
										href="javascript:page_nav(document.forms[0],${pages.currentPageNo-1});"
										aria-controls="datatable-responsive" data-dt-idx="1"
										tabindex="0">上一页</a>
									</li>
								</c:if>
								<c:if test="${pages.currentPageNo < pages.totalPageCount }">
									<li class="paginate_button "><a
										href="javascript:page_nav(document.forms[0],${pages.currentPageNo+1 });"
										aria-controls="datatable-responsive" data-dt-idx="1"
										tabindex="0">下一页</a>
									</li>
									<li class="paginate_button next"><a
										href="javascript:page_nav(document.forms[0],${pages.totalPageCount });"
										aria-controls="datatable-responsive" data-dt-idx="7"
										tabindex="0">最后一页</a>
									</li>
								</c:if>
							</ul>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</div>
</div>
<%@include file="common/footer.jsp"%>
<script src="${pageContext.request.contextPath }/statics/localjs/rollpage.js"></script>

<script src="${pageContext.request.contextPath }/statics/localjs/DataDictionaryinfolist.js"></script>